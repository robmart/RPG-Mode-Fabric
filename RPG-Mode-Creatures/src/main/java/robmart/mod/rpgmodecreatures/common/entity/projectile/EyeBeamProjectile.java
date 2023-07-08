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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import robmart.mod.rpgmodecreatures.common.entity.IVariants;
import robmart.mod.rpgmodecreatures.common.entity.RPGCreaturesEntityTypes;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGEffectsStatusEffects;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EyeBeamProjectile extends ExplosiveProjectileEntity implements GeoAnimatable, IVariants<Integer> {
    public static final TrackedData<Integer> VARIANT = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int tickCounter = 0;

    public EyeBeamProjectile(EntityType<EyeBeamProjectile> eyeBeamProjectileEntityType, World world) {
        super(eyeBeamProjectileEntityType, world);
    }

    public EyeBeamProjectile(World world, MobEntity owner, double directionX, double directionY, double directionZ) {
        super(RPGCreaturesEntityTypes.BEHOLDER_EYE_BEAM, owner, directionX, directionY, directionZ, world);
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
        Entity entity = this.getOwner();
        if (this.world.isClient || (entity == null || !entity.isRemoved()) && this.world.isChunkLoaded(this.getBlockPos())) {
            super.tick();
            if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
                updateRotation();
            }

            Vec3d vec3d = this.getVelocity();
            double d = vec3d.x;
            double e = vec3d.y;
            double g = vec3d.z;
            double l = vec3d.horizontalLength();

            this.setYaw((float) (MathHelper.atan2(d, g) * 57.2957763671875D));
            this.setPitch((float) (MathHelper.atan2(e, l) * 57.2957763671875D));
            this.setPitch(updateRotation(this.prevPitch, this.getPitch()));
            this.setYaw(updateRotation(this.prevYaw, this.getYaw()));

            this.checkBlockCollision();
        }

        if (world.isClient)
            tickCounter++;
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
        return true;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            World.ExplosionSourceType explosionSourceType = World.ExplosionSourceType.MOB;
            List<Entity> entities;

            int i = 0;
            if (this.world.getDifficulty() == Difficulty.EASY) {
                i = 10;
            } else if (this.world.getDifficulty() == Difficulty.NORMAL) {
                i = 20;
            } else if (this.world.getDifficulty() == Difficulty.HARD) {
                i = 40;
            }

            switch (this.getVariant()) {
                default:
                    break;
                case 1: //Charm
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(RPGEffectsStatusEffects.CHARM, 20 * finalI / 2), this.getEffectCause());
                    });
                    break;
                case 2: //Nausea
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI1 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * finalI1), this.getEffectCause());
                    });
                    break;
                case 3: //Poison/Hunger/Weakness
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI2 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * finalI2, 1), this.getEffectCause());
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20 * finalI2), this.getEffectCause());
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * finalI2 * 2), this.getEffectCause());
                    });
                    break;
                case 4: //Slow
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI3 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * finalI3 / 2, 2), this.getEffectCause());
                    });
                    break;
                case 5: //Wither/Explosion
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI4 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.damage(DamageSource.magic(this, getOwner()), 5.0F);
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20 * finalI4, 1), this.getEffectCause());
                    });
                    this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), 2.0F, false, explosionSourceType);
                    break;
                case 6: //Yeet
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        if (livingEntity instanceof PlayerEntity) System.out.println(world.isClient + " P");
                        double rangeMin = -0.5;
                        double rangeMax = 0.5;
                        double x = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
                        double y = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
                        double z = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
                        livingEntity.setVelocity(x, 1.5 + y, z);
                        livingEntity.velocityModified = true;
                    });
                    break;
                case 7: //Sleep
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI6 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(RPGEffectsStatusEffects.SLEEP, 20 * finalI6 * 2, 1), this.getEffectCause());
                    });
                    break;
                case 8: //Petrification
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    int finalI7 = i;
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.addStatusEffect(new StatusEffectInstance(RPGEffectsStatusEffects.PETRIFICATION, 20 * finalI7 / 4, 1), this.getEffectCause());
                    });
                    break;
                case 9: //Disintegration
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        Random random = livingEntity.getRandom();
                        int randomNum = random.nextInt(6) + 1;
                        EquipmentSlot slot = switch (randomNum) {
                            case 1 -> EquipmentSlot.HEAD;
                            case 2 -> EquipmentSlot.CHEST;
                            case 3 -> EquipmentSlot.LEGS;
                            case 4 -> EquipmentSlot.FEET;
                            case 5 -> EquipmentSlot.MAINHAND;
                            case 6 -> EquipmentSlot.OFFHAND;
                            default -> null;
                        };

                        if (livingEntity.hasStackEquipped(slot)) {
                            livingEntity.getEquippedStack(slot).damage(livingEntity.getEquippedStack(slot).getMaxDamage() / 10, livingEntity, (e) -> e.sendEquipmentBreakStatus(slot));
                        }

                        livingEntity.damage(DamageSource.magic(this, getOwner()), 10F);
                    });
                    break;
                case 10: //Death ray
                    entities = world.getOtherEntities(this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D), (entity -> entity instanceof LivingEntity));
                    entities.forEach(entity -> {
                        if (entity == this.getOwner()) return;

                        LivingEntity livingEntity = (LivingEntity) entity;
                        livingEntity.damage(DamageSource.magic(this, getOwner()), 15.0F);
                    });
                    break;
            }

            this.discard();
        }
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.WITCH;
    }

    private PlayState animationPredicate(AnimationState<EyeBeamProjectile> animationState) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return tickCounter;
    }
}
