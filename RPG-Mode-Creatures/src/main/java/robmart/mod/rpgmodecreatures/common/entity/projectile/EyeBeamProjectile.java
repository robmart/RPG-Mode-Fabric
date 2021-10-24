package robmart.mod.rpgmodecreatures.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import robmart.mod.rpgmodecreatures.common.RPGModeCreatures;
import robmart.mod.rpgmodecreatures.common.entity.IVariants;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffects;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class EyeBeamProjectile extends ExplosiveProjectileEntity implements IAnimatable, IVariants<Integer> {
    public static final TrackedData<Integer> VARIANT = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final AnimationFactory factory = new AnimationFactory(this);

    public EyeBeamProjectile(EntityType<EyeBeamProjectile> eyeBeamProjectileEntityType, World world) {
        super(eyeBeamProjectileEntityType, world);
    }

    public EyeBeamProjectile(World world, MobEntity owner, double directionX, double directionY, double directionZ) {
        super(RPGModeCreatures.BEHOLDER_EYE_BEAM, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(VARIANT, 1);
    }

    @Override
    public void setVariant(Integer variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    @Override
    public Integer getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3d vec3d;

        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            updateRotation();
        }

        vec3d = this.getVelocity();
        double d = vec3d.x;
        double e = vec3d.y;
        double g = vec3d.z;
        double h = this.getX() + d;
        double j = this.getY() + e;
        double k = this.getZ() + g;
        double l = vec3d.horizontalLength();

        this.setYaw((float) (MathHelper.atan2(d, g) * 57.2957763671875D));
        this.setPitch( (float) (MathHelper.atan2(e, l) * 57.2957763671875D));
        this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
        this.setYaw(updateRotation(this.prevYaw, this.getYaw()));
        float m = 0.99F;

        this.setVelocity(vec3d.multiply(m));

        this.updatePosition(h, j, k);
        this.checkBlockCollision();
    }

    @Override
    public void setYaw(float yaw) {
        super.setYaw(yaw);

        this.prevYaw = yaw;
    }

    @Override
    public void setPitch(float pitch) {
        super.setPitch(pitch);

        this.prevPitch = pitch;
    }

    public boolean collides() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
        List<Entity> entities;

        int i = 0;
        if (this.world.getDifficulty() == Difficulty.NORMAL) {
            i = 20;
        } else if (this.world.getDifficulty() == Difficulty.HARD) {
            i = 40;
        }

        switch (this.getVariant()) {
            default:
                break;
            case 1: //Charm
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                int finalI = i;
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.addStatusEffect(new StatusEffectInstance(RPGStatusEffects.CHARM, 20 * finalI / 2), this.getEffectCause());
                });
                break;
            case 4: //Slow
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                int finalI1 = i;
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * finalI1 / 2 , 2), this.getEffectCause());
                });
                break;
            case 5: //Wither/Explosion
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                int finalI2 = i;
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.damage(DamageSource.MAGIC, 5.0F);
                    livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20 * finalI2, 1), this.getEffectCause());
                });
                this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 2.0F, false, destructionType);
                break;
            case 6: //Yeet
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.setVelocity(0, 1.5, 0);
                });
                break;
            case 8: //Petrification
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                int finalI3 = i;
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.addStatusEffect(new StatusEffectInstance(RPGStatusEffects.PETRIFICATION, 20 * finalI3 / 4, 1), this.getEffectCause());
                });
                break;
            case 9: //Disintegration
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    Random random = livingEntity.getRandom();
                    int randomNum = random.nextInt(4) + 1;

                    switch (randomNum) {
                        case 1:
                            if (livingEntity.hasStackEquipped(EquipmentSlot.HEAD)) {
                                livingEntity.getEquippedStack(EquipmentSlot.HEAD).damage(livingEntity.getEquippedStack(EquipmentSlot.HEAD).getMaxDamage() / 10, livingEntity, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.HEAD));
                            }
                            break;
                        case 2:
                            if (livingEntity.hasStackEquipped(EquipmentSlot.CHEST)) {
                                livingEntity.getEquippedStack(EquipmentSlot.CHEST).damage(livingEntity.getEquippedStack(EquipmentSlot.CHEST).getMaxDamage() / 10, livingEntity, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.CHEST));
                            }
                            break;
                        case 3:
                            if (livingEntity.hasStackEquipped(EquipmentSlot.LEGS)) {
                                livingEntity.getEquippedStack(EquipmentSlot.LEGS).damage(livingEntity.getEquippedStack(EquipmentSlot.LEGS).getMaxDamage() / 10, livingEntity, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.LEGS));
                            }
                            break;
                        case 4:
                            if (livingEntity.hasStackEquipped(EquipmentSlot.FEET)) {
                                livingEntity.getEquippedStack(EquipmentSlot.FEET).damage(livingEntity.getEquippedStack(EquipmentSlot.FEET).getMaxDamage() / 10, livingEntity, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.FEET));
                            }
                            break;
                    }

                    livingEntity.damage(DamageSource.MAGIC, 10F);
                });
                break;
            case 10: //Death ray
                if (this.world.isClient) return;
                entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                entities.forEach(entity -> {
                    if (entity == this.getOwner()) return;

                    LivingEntity livingEntity = (LivingEntity) entity;
                    livingEntity.damage(DamageSource.MAGIC, 15.0F);
                });
                break;
        }

        this.discard();
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
