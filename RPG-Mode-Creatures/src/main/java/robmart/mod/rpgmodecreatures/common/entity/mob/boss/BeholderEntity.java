package robmart.mod.rpgmodecreatures.common.entity.mob.boss;

import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import robmart.mod.rpgmodecreatures.common.RPGModeCreatures;
import robmart.mod.rpgmodecreatures.common.entity.RPGModeEntityGroup;
import robmart.mod.rpgmodecreatures.common.entity.ai.goal.FollowTargetHeightGoal;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
import robmart.mod.rpgmodecreatures.common.helper.RPGMathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class BeholderEntity extends HostileEntity implements Monster, IAnimatable, RangedAttackMob {
    private final AnimationFactory factory = new AnimationFactory(this);

    private final ServerBossBar bossBar;

    public BeholderEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);

        this.moveControl = new BeholderMoveControl(this);
        this.lookControl = new BeholderLookControl(this);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
    }

    protected void initGoals(){
        this.goalSelector.add(2, new ShootBeamGoal(this));
        this.goalSelector.add(3, new LookAtTargetGoal(this));
        this.goalSelector.add(5, new FlyRandomlyGoal(this));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new FollowTargetHeightGoal(this, PlayerEntity.class, true, false));
        this.targetSelector.add(1, new FollowTargetHeightGoal(this, PigEntity.class, true, false));

    }

    public static DefaultAttributeContainer.Builder createBeholderAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8D);
    }

    protected BodyControl createBodyControl() {
        return new BeholderBodyControl(this);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    public void travel(Vec3d movementInput) {
        if (this.isTouchingWater()) {
            this.updateVelocity(0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.800000011920929D));
        } else if (this.isInLava()) {
            this.updateVelocity(0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.5D));
        } else {
            float f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ())).getBlock().getSlipperiness() * 0.91F;
            }

            float g = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ())).getBlock().getSlipperiness() * 0.91F;
            }

            this.updateVelocity(this.onGround ? 0.1F * g : 0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply((double)f));
        }

        this.updateLimbs(this, false);
    }

    public boolean isClimbing() {
        return false;
    }


    @Override
    protected void mobTick() {
        super.mobTick();

        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    public EntityGroup getGroup() {
        return RPGModeEntityGroup.ABERRATION;
    }

    protected boolean canStartRiding(Entity entity) {
        return false;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        Random random = this.random;
        int eye = random.nextInt(10) + 1;
        shootBeamAt(10, target);
    }

    private void shootBeamAt(int eyeIndex, LivingEntity target) {
        this.shootBeamAt(eyeIndex, target.getX(), target.getY() + (double)target.getStandingEyeHeight() * 0.5D, target.getZ());
    }

    private void shootBeamAt(int eyeIndex, double targetX, double targetY, double targetZ) {
        if (!this.isSilent()) {
            this.world.syncWorldEvent(null, WorldEvents.WITHER_SHOOTS, this.getBlockPos(), 0);
        }

        Vec3d relPos;
        switch (eyeIndex) {
            default -> relPos = new Vec3d(0, 0, 0);
            case 1 -> relPos = new Vec3d(-3, 0, 1);
            case 2 -> relPos = new Vec3d(-2.6, 0.4, 0.7);
            case 3 -> relPos = new Vec3d(-2.6, 1.5, 1.1);
            case 4 -> relPos = new Vec3d(-2.35, 2.8, 1.2);
            case 5 -> relPos = new Vec3d(-1, 3.3, 1.2);
            case 6 -> relPos = new Vec3d(0.2, 3.5, 1.8);
            case 7 -> relPos = new Vec3d(1.2, 2.85, 1.55);
            case 8 -> relPos = new Vec3d(2.45, 2.55, 1.55);
            case 9 -> relPos = new Vec3d(2.45, 1, 1.2);
            case 10 -> relPos = new Vec3d(2.65, 0, 2);
        }

        Vec3d pos = RPGMathHelper.toAbsolutePos(new Vec3d(this.getX(), this.getY(), this.getZ()), relPos, new Vec2f(this.getPitch(), this.getYaw()));
        double g = targetX - pos.getX();
        double h = targetY - pos.getY();
        double i = targetZ - pos.getZ();
        EyeBeamProjectile eyeBeamProjectile = new EyeBeamProjectile(this.world, this, g, h, i);
        eyeBeamProjectile.setOwner(this);
        eyeBeamProjectile.setPos(pos.getX(), pos.getY(), pos.getZ());
        eyeBeamProjectile.setVariant(eyeIndex);

        this.world.spawnEntity(eyeBeamProjectile);
    }


    static class BeholderMoveControl extends MoveControl {
        private final BeholderEntity beholder;
        private int collisionCheckCooldown;

        public BeholderMoveControl(BeholderEntity beholder) {
            super(beholder);
            this.beholder = beholder;
        }

        public void tick() {
            if (this.state == MoveControl.State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.beholder.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.beholder.getX(), this.targetY - this.beholder.getY(), this.targetZ - this.beholder.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.beholder.setVelocity(this.beholder.getVelocity().add(vec3d.multiply(0.1D)));
                    } else {
                        this.state = MoveControl.State.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.beholder.getBoundingBox();

            for(int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.beholder.world.isSpaceEmpty(this.beholder, box)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class BeholderLookControl extends LookControl {
        public BeholderLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
        }
    }

    private class BeholderBodyControl extends BodyControl {
        public BeholderBodyControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            BeholderEntity.this.headYaw = BeholderEntity.this.bodyYaw;
            BeholderEntity.this.bodyYaw = BeholderEntity.this.getYaw();
        }
    }

    static class BeholderProjectileAttackGoal extends ProjectileAttackGoal {
        BeholderEntity beholder;

        public BeholderProjectileAttackGoal(BeholderEntity mob, double mobSpeed, int intervalTicks, float maxShootRange) {
            this(mob, mobSpeed, intervalTicks, intervalTicks, maxShootRange);
        }

        public BeholderProjectileAttackGoal(BeholderEntity mob, double mobSpeed, int minIntervalTicks, int maxIntervalTicks, float maxShootRange) {
            super(mob, mobSpeed, minIntervalTicks, maxIntervalTicks, maxShootRange);

            this.beholder = mob;
        }
    }

    private static class LookAtTargetGoal extends Goal {
        private final BeholderEntity beholder;

        public LookAtTargetGoal(BeholderEntity beholder) {
            this.beholder = beholder;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public void tick() {
            if (this.beholder.getTarget() == null) {
                Vec3d vec3d = this.beholder.getVelocity();
                this.beholder.setYaw(-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
                this.beholder.bodyYaw = this.beholder.getYaw();
            } else {
                LivingEntity livingEntity = this.beholder.getTarget();
                double d = 64.0D;
                if (livingEntity.squaredDistanceTo(this.beholder) < 4096.0D) {
                    double e = livingEntity.getX() - this.beholder.getX();
                    double f = livingEntity.getZ() - this.beholder.getZ();
                    this.beholder.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.beholder.bodyYaw = this.beholder.getYaw();
                }
            }

        }
    }

    private static class ShootBeamGoal extends Goal {
        private final BeholderEntity beholder;
        public int cooldown;

        public ShootBeamGoal(BeholderEntity beholder) {
            this.beholder = beholder;
            this.setControls(EnumSet.of(Goal.Control.LOOK));
        }

        public boolean canStart() {
            return this.beholder.getTarget() != null;
        }

        public void start() {
            this.cooldown = 0;
        }

        public void stop() {
        }

        public void tick() {
            LivingEntity livingEntity = this.beholder.getTarget();
            if (livingEntity.squaredDistanceTo(this.beholder) < 4096.0D && this.beholder.canSee(livingEntity)) {
                ++this.cooldown;

                if (this.cooldown == 20) {
                    beholder.attack(livingEntity, 0f);
                    this.cooldown = -40;
                }
            } else if (this.cooldown > 0) {
                --this.cooldown;
            }

            if (this.beholder.getTarget() == null) {
                Vec3d vec3d = this.beholder.getVelocity();
                this.beholder.setYaw(-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
                this.beholder.bodyYaw = this.beholder.getYaw();
            } else {
                double d = 64.0D;
                if (livingEntity.squaredDistanceTo(this.beholder) < 4096.0D) {
                    double e = livingEntity.getX() - this.beholder.getX();
                    double f = livingEntity.getZ() - this.beholder.getZ();
                    this.beholder.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.beholder.bodyYaw = this.beholder.getYaw();
                }
            }
        }
    }

    private static class FlyRandomlyGoal extends Goal {
        private final BeholderEntity beholder;

        public FlyRandomlyGoal(BeholderEntity beholder) {
            this.beholder = beholder;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            MoveControl moveControl = this.beholder.getMoveControl();

            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.beholder.getX();
                double e = moveControl.getTargetY() - this.beholder.getY();
                double f = moveControl.getTargetZ() - this.beholder.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0D || g > 3600.0D;
            }
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            Random random = this.beholder.getRandom();
            double d;
            if (beholder.getTarget() != null && beholder.getX() > (beholder.getTarget().getX() + 15)) {
                d = this.beholder.getX() + (double)((random.nextFloat() * 2.0F) * -1F);
            } else if (beholder.getTarget() != null && beholder.getX() < (beholder.getTarget().getX() - 15)) {
                d = this.beholder.getX() + (double)((random.nextFloat() * 2.0F) * 1F);
            } else if (beholder.getTarget() != null) {
                d = this.beholder.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 1.5F);
            } else {
                d = this.beholder.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 10F);
            }

            double e;
            if (beholder.getTarget() != null && beholder.getY() > (beholder.getTarget().getY() + 5)) {
                e = this.beholder.getY() + (double)((random.nextFloat() * 2.0F) * -0.5F);
            } else if (beholder.getTarget() != null && beholder.getY() < (beholder.getTarget().getY() - 5)) {
                e = this.beholder.getY() + (double)((random.nextFloat() * 2.0F) * 0.5F);
            } else {
                e = this.beholder.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 0.5F);
            }

            double f;
            if (beholder.getTarget() != null && beholder.getZ() > (beholder.getTarget().getZ() + 15)) {
                f = this.beholder.getZ() + (double)((random.nextFloat() * 2.0F) * -1F);
            } else if (beholder.getTarget() != null && beholder.getZ() < (beholder.getTarget().getZ() - 15)) {
                f = this.beholder.getZ() + (double)((random.nextFloat() * 2.0F) * 1F);
            } else if (beholder.getTarget() != null) {
                f = this.beholder.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 1.5F);
            } else {
                f = this.beholder.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 10F);
            }

            this.beholder.getMoveControl().moveTo(d, e, f, 0.25D);
        }
    }

}
