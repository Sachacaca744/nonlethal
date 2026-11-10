package com.nonlethal;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlashballItem extends Item {
    public FlashballItem(Properties p) { super(p); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean creative = player.getAbilities().instabuild;
        ItemStack ammo = ItemStack.EMPTY;
        for (ItemStack s : player.getInventory().items) {
            if (s.is(NonLethal.RUBBER_BULLET.get())) { ammo = s; break; }
        }
        if (ammo.isEmpty() && !creative) return InteractionResultHolder.fail(stack);
        if (!level.isClientSide) {
            RubberBulletEntity b = new RubberBulletEntity(level, player);
            b.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2.5f, 1f);
            level.addFreshEntity(b);
            if (level instanceof net.minecraft.server.level.ServerLevel sl) {
                net.minecraft.world.phys.Vec3 m = player.getEyePosition().add(player.getLookAngle().scale(1.2));
                sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE, m.x, m.y - 0.15, m.z, 10, 0.05, 0.05, 0.05, 0.02);
                sl.sendParticles(net.minecraft.core.particles.ParticleTypes.FLAME, m.x, m.y - 0.15, m.z, 3, 0.03, 0.03, 0.03, 0.01);
            }
            level.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_LAUNCH, SoundSource.PLAYERS, 1f, 1.4f);
            if (!creative) {
                ammo.shrink(1);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
        }
        player.getCooldowns().addCooldown(this, 25);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
