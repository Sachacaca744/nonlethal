package com.nonlethal;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RubberBulletEntity extends ThrowableItemProjectile {
    public RubberBulletEntity(EntityType<? extends RubberBulletEntity> type, Level level) { super(type, level); }
    public RubberBulletEntity(Level level, LivingEntity shooter) { super(NonLethal.BULLET.get(), shooter, level); }

    @Override protected Item getDefaultItem() { return NonLethal.RUBBER_BULLET.get(); }
    @Override protected float getGravity() { return 0.015f; }

    @Override
    protected void onHitEntity(EntityHitResult r) {
        super.onHitEntity(r);
        Entity t = r.getEntity();
        // Non létal : ne descend jamais la cible sous 0,5 coeur
        float dmg = 3f;
        if (t instanceof LivingEntity l) dmg = Math.min(3f, Math.max(0f, l.getHealth() - 1f));
        if (dmg > 0f) t.hurt(damageSources().thrown(this, getOwner()), dmg);
        if (t instanceof LivingEntity l) l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
    }

    @Override
    protected void onHit(HitResult r) {
        super.onHit(r);
        if (!level().isClientSide) discard();
    }
}
