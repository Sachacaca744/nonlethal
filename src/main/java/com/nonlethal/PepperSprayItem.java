package com.nonlethal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PepperSprayItem extends Item {
    public PepperSprayItem(Properties p) { super(p); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            Vec3 eye = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            ServerLevel sl = (ServerLevel) level;
            for (int i = 1; i <= 8; i++) {
                Vec3 p = eye.add(look.scale(i * 0.5));
                sl.sendParticles(ParticleTypes.CLOUD, p.x, p.y, p.z, 3, 0.15, 0.15, 0.15, 0.01);
            }
            AABB box = player.getBoundingBox().expandTowards(look.scale(4)).inflate(1.5);
            for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, t -> t != player)) {
                Vec3 to = e.getEyePosition().subtract(eye);
                if (to.length() <= 4.5 && to.normalize().dot(look) > 0.8) {
                    e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                    e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 140, 0));
                    e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                }
            }
            level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1f, 1.5f);
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
        player.getCooldowns().addCooldown(this, 40);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
