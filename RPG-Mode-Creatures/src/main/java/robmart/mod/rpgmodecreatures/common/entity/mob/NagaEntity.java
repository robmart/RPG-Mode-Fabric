package robmart.mod.rpgmodecreatures.common.entity.mob;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;
import robmart.mod.rpgmodecreatures.common.entity.IVariants;
import robmart.mod.rpgmodecreatures.common.entity.RPGEntityGroup;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NagaEntity extends HostileEntity implements GeoAnimatable, IVariants, RangedAttackMob {
    public static List<Optional<BiomeKeys>> biomeList = List.of(new Optional[]{
            Optional.of(BiomeKeys.OCEAN),
            Optional.of(BiomeKeys.SWAMP),
            Optional.of(BiomeKeys.MANGROVE_SWAMP),
            Optional.of(BiomeKeys.RIVER),
            Optional.of(BiomeKeys.FROZEN_OCEAN),
            Optional.of(BiomeKeys.FROZEN_RIVER),
            Optional.of(BiomeKeys.BEACH),
            Optional.of(BiomeKeys.DEEP_OCEAN),
            Optional.of(BiomeKeys.STONY_SHORE),
            Optional.of(BiomeKeys.SNOWY_BEACH),
            Optional.of(BiomeKeys.WARM_OCEAN),
            Optional.of(BiomeKeys.LUKEWARM_OCEAN),
            Optional.of(BiomeKeys.COLD_OCEAN),
            Optional.of(BiomeKeys.DEEP_LUKEWARM_OCEAN),
            Optional.of(BiomeKeys.DEEP_COLD_OCEAN),
            Optional.of(BiomeKeys.DEEP_FROZEN_OCEAN)
    });

    public static final TrackedData<String> VARIANT = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.STRING);
    public static final TrackedData<Boolean> ATTACK = DataTracker.registerData(NagaEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;

    private static final ImmutableMap<EntityPose, EntityDimensions> POSE_DIMENSIONS;

    boolean targetingUnderwater;
    int poseCounter = 100;
    private int tickCounter = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation BOW_ANIM = RawAnimation.begin().thenPlay("animation.naga.bow");
    public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("animation.naga.idle");
    public static final RawAnimation IDLE_WATER_ANIM = RawAnimation.begin().thenPlay("animation.naga.idle_water");
    public static final RawAnimation NONE_ANIM = RawAnimation.begin().thenPlay("animation.naga.none");

    static {
        POSE_DIMENSIONS = ImmutableMap.of(EntityPose.STANDING, EntityDimensions.changing(0.75f, 2.25f),
                EntityPose.SWIMMING, EntityDimensions.changing(0.8F, 0.7F));
    }

    public NagaEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new NagaMoveControl(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }

    @Override
    protected void initGoals() {
        int counter = 0;
        this.goalSelector.add(1, new WanderAroundOnSurfaceGoal(this, 1.0D));
        this.goalSelector.add(2, new TridentAttackGoal(this, 1.0D, 40, 10.0F));
        this.goalSelector.add(2, new NagaMeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(2, new BowAttackGoal<>(this, 1.0D, 20, 15.0F));
        this.goalSelector.add(5, new LeaveWaterGoal(this, 1.0D));
        this.goalSelector.add(6, new TargetAboveWaterGoal(this, 1.0D, this.world.getSeaLevel()));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D));

        counter = 1;
        this.targetSelector.add(counter++, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(counter, new ActiveTargetGoal<>(this, PlayerEntity.class, true, false));
        this.targetSelector.add(counter++, new ActiveTargetGoal<>(this, PigEntity.class, true, false));

    }

    public static DefaultAttributeContainer.Builder createNagaAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ARMOR, 2D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                ;
    }

    @Override
    public EntityGroup getGroup() {
        return RPGEntityGroup.WATER;
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return POSE_DIMENSIONS.getOrDefault(pose, EntityDimensions.changing(0.75f, 2.25f));
    }

    public ImmutableList<EntityPose> getPoses() {
        return ImmutableList.of(EntityPose.STANDING, EntityPose.SWIMMING);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    boolean isTargetingUnderwater() {
        if (this.targetingUnderwater) {
            return true;
        } else {
            LivingEntity livingEntity = this.getTarget();
            return livingEntity != null && livingEntity.isTouchingWater();
        }
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater()) {
            this.updateVelocity(0.01F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.95D));
        } else {
            super.travel(movementInput);
        }

    }

    @Override
    public void tick() {
        super.tick();
        poseCounter--;

        updatePose();

        if (world.isClient)
            tickCounter++;
    }

    public void updatePose() {
        if (poseCounter > 0) return;

        if (this.getPose() == EntityPose.STANDING) {
            if (isSwimming()) {
                setPose(EntityPose.SWIMMING);
                poseCounter = 100;
            }
        } else if (this.getPose() == EntityPose.SWIMMING) {
            if (!isSwimming()) {
                setPose(EntityPose.STANDING);
                poseCounter = 100;
            }
        } else {
            setPose(EntityPose.STANDING);
        }
    }

    public boolean isSubmergedInWater() {
        return this.submergedInWater && this.isTouchingWater();
    }

    public void updateSwimming() {
        if (!this.world.isClient) {

            if (this.canMoveVoluntarily() && this.isTouchingWater() && (this.isTargetingUnderwater() || this.getTarget() == null)) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.setSwimming(false);
            }
        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idleController", 0, this::idleAnimationPredicate));
        controllers.add(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    private PlayState idleAnimationPredicate(AnimationState<NagaEntity> animationState) {
        if (getPose() == EntityPose.STANDING)
            animationState.getController().setAnimation(IDLE_ANIM);
        else
            animationState.getController().setAnimation(IDLE_WATER_ANIM);

        return PlayState.CONTINUE;
    }

    private PlayState animationPredicate(AnimationState<NagaEntity> animationState) {
        if (getAttack()) {
            animationState.getController().setAnimation(BOW_ANIM);
        } else if (animationState.getController().getAnimationState() != AnimationController.State.STOPPED) {
            animationState.getController().setAnimation(NONE_ANIM);
            animationState.getController().stop();
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return tickCounter;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, "blue");
        this.dataTracker.startTracking(ATTACK, false);
    }

    @Override
    public EntityData initialize(ServerWorldAccess serverWorldAccess, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
        int rand = this.random.nextInt(2);
        this.setVariant(rand == 0 ? "blue" : "green");

        this.initEquipment(random, difficulty);

        return super.initialize(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected void initEquipment(net.minecraft.util.math.random.Random random, LocalDifficulty difficulty) {
        if ((double)this.random.nextFloat() > 0.2D) {
            int i = this.random.nextInt(16);
            if (i < 5) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else if (i < 10) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
        }

    }

    public static boolean canSpawn(EntityType<NagaEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, net.minecraft.util.math.random.Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.getFluidState(pos).isIn(FluidTags.WATER));
    }

    @Override
    public boolean canSpawn(WorldView world) {
        return world.doesNotIntersectEntities(this);
    }

    private static boolean isValidSpawnDepth(WorldAccess world, BlockPos pos) {
        return pos.getY() < world.getSeaLevel() - 5;
    }

    protected boolean prefersNewEquipment(ItemStack newStack, ItemStack oldStack) {
        if (oldStack.isOf(Items.TRIDENT)) {
            if (newStack.isOf(Items.TRIDENT)) {
                return newStack.getDamage() < oldStack.getDamage();
            } else {
                return false;
            }
        } else if (oldStack.isOf(Items.BOW)) {
            if (newStack.isOf(Items.BOW)) {
                return newStack.getDamage() < oldStack.getDamage();
            } else {
                return false;
            }
        }  else if (oldStack.isOf(Items.IRON_SWORD)) {
            if (newStack.isOf(Items.IRON_SWORD)) {
                return newStack.getDamage() < oldStack.getDamage();
            } else {
                return false;
            }
        } else {
            return newStack.isOf(Items.TRIDENT) || super.prefersNewEquipment(newStack, oldStack);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setVariant(tag.getString("Variant"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putString("Variant", getVariant());
    }

    @Override
    public void setVariant(String variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    @Override
    public String getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setAttack(boolean attack) {
        this.dataTracker.set(ATTACK, attack);
    }

    public boolean getAttack() {
        return this.dataTracker.get(ATTACK);
    }

    public void setTargetingUnderwater(boolean targetingUnderwater) {
        this.targetingUnderwater = targetingUnderwater;
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (getMainHandStack().isOf(Items.TRIDENT)) {
            TridentEntity tridentEntity = new TridentEntity(this.world, this, getMainHandStack());
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333D) - tridentEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            tridentEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
            this.world.spawnEntity(tridentEntity);
        } else {
            ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));

            PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
            this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.world.spawnEntity(persistentProjectileEntity);

        }
    }

    protected boolean hasFinishedCurrentPath() {
        Path path = this.getNavigation().getCurrentPath();
        if (path != null) {
            BlockPos blockPos = path.getTarget();
            if (blockPos != null) {
                double d = this.squaredDistanceTo((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
                if (d < 4.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return !this.isSwimming();
    }



    static class NagaMoveControl extends MoveControl {
        private final NagaEntity naga;

        public NagaMoveControl(NagaEntity naga) {
            super(naga);
            this.naga = naga;
        }

        public void tick() {
            LivingEntity livingEntity = this.naga.getTarget();
            if (this.naga.isTouchingWater()) {
                if (livingEntity != null && livingEntity.getY() > this.naga.getY() || this.naga.targetingUnderwater) {
                    this.naga.setVelocity(this.naga.getVelocity().add(0.0D, 0.002D, 0.0D));
                }

                if (this.state != MoveControl.State.MOVE_TO || this.naga.getNavigation().isIdle()) {
                    this.naga.setMovementSpeed(0.0F);
                    return;
                }

                double d = this.targetX - this.naga.getX();
                double e = this.targetY - this.naga.getY();
                double f = this.targetZ - this.naga.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F;
                this.naga.setYaw(this.wrapDegrees(this.naga.getYaw(), h, 90.0F));
                this.naga.bodyYaw = this.naga.getYaw();
                float i = (float)(this.speed * this.naga.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125F, this.naga.getMovementSpeed(), i);
                this.naga.setMovementSpeed(j);
                this.naga.setVelocity(this.naga.getVelocity().add((double)j * d * 0.005D, (double)j * e * 0.1D, (double)j * f * 0.005D));
            } else {
                super.tick();
            }

        }
    }

    private static class WanderAroundOnSurfaceGoal extends Goal {
        private final PathAwareEntity mob;
        private double x;
        private double y;
        private double z;
        private final double speed;
        private final World world;

        public WanderAroundOnSurfaceGoal(PathAwareEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.world = mob.world;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        public boolean canStart() {
            if (!this.world.isDay()) {
                return false;
            } else if (this.mob.isTouchingWater()) {
                return false;
            } else {
                Vec3d vec3d = this.getWanderTarget();
                if (vec3d == null) {
                    return false;
                } else {
                    this.x = vec3d.x;
                    this.y = vec3d.y;
                    this.z = vec3d.z;
                    return true;
                }
            }
        }

        public boolean shouldContinue() {
            return !this.mob.getNavigation().isIdle();
        }

        public void start() {
            this.mob.getNavigation().startMovingTo(this.x, this.y, this.z, this.speed);
        }

        @Nullable
        private Vec3d getWanderTarget() {
            net.minecraft.util.math.random.Random random = this.mob.getRandom();
            BlockPos blockPos = this.mob.getBlockPos();

            for(int i = 0; i < 10; ++i) {
                BlockPos blockPos2 = blockPos.add(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (this.world.getBlockState(blockPos2).isOf(Blocks.WATER)) {
                    return Vec3d.ofBottomCenter(blockPos2);
                }
            }

            return null;
        }
    }

    private static class TridentAttackGoal extends ProjectileAttackGoal {
        private final NagaEntity naga;

        public TridentAttackGoal(RangedAttackMob rangedAttackMob, double d, int i, float f) {
            super(rangedAttackMob, d, i, f);
            this.naga = (NagaEntity) rangedAttackMob;
        }

        public boolean canStart() {
            return super.canStart() && this.naga.getMainHandStack().isOf(Items.TRIDENT);
        }

        public void start() {
            super.start();
            this.naga.setAttack(true);
            this.naga.setCurrentHand(Hand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.naga.clearActiveItem();
            this.naga.setAttack(false);
        }
    }

    private static class BowAttackGoal<T extends HostileEntity & RangedAttackMob> extends net.minecraft.entity.ai.goal.BowAttackGoal<T> {
        private final NagaEntity naga;

        public BowAttackGoal(T actor, double speed, int attackInterval, float range) {
            super(actor, speed, attackInterval, range);
            this.naga = (NagaEntity) actor;
        }

        public void start() {
            super.start();
            this.naga.setAttack(true);
        }

        public void stop() {
            super.stop();
            this.naga.setAttack(false);
        }
    }

    private static class NagaMeleeAttackGoal extends MeleeAttackGoal {
        private final NagaEntity naga;

        public NagaMeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.naga = (NagaEntity) mob;
        }

        public boolean canStart() {
            return super.canStart() && !this.naga.getMainHandStack().isOf(Items.TRIDENT) && !this.naga.getMainHandStack().isOf(Items.BOW);
        }

        public void start() {
            super.start();
            this.naga.setAttacking(true);
            this.naga.setCurrentHand(Hand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.naga.clearActiveItem();
            this.naga.setAttacking(false);
        }
    }

    static class LeaveWaterGoal extends MoveToTargetPosGoal {
        private final NagaEntity naga;

        public LeaveWaterGoal(NagaEntity naga, double speed) {
            super(naga, speed, 8, 2);
            this.naga = naga;
        }

        public boolean canStart() {
            return super.canStart() && !this.naga.world.isDay() && this.naga.isTouchingWater() && this.naga.getY() >= (double)(this.naga.world.getSeaLevel() - 3);
        }

        public boolean shouldContinue() {
            return super.shouldContinue();
        }

        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockPos blockPos = pos.up();
            return world.isAir(blockPos) && world.isAir(blockPos.up()) && world.getBlockState(pos).hasSolidTopSurface(world, pos, this.naga);
        }

        public void start() {
            this.naga.setTargetingUnderwater(false);
            this.naga.navigation = this.naga.landNavigation;
            super.start();
        }

        public void stop() {
            super.stop();
        }
    }

    private static class TargetAboveWaterGoal extends Goal {
        private final NagaEntity naga;
        private final double speed;
        private final int minY;
        private boolean foundTarget;

        public TargetAboveWaterGoal(NagaEntity naga, double speed, int minY) {
            this.naga = naga;
            this.speed = speed;
            this.minY = minY;
        }

        public boolean canStart() {
            return !this.naga.world.isDay() && this.naga.isTouchingWater() && this.naga.getY() < (double)(this.minY - 2);
        }

        public boolean shouldContinue() {
            return this.canStart() && !this.foundTarget;
        }

        public void tick() {
            if (this.naga.getY() < (double)(this.minY - 1) && (this.naga.getNavigation().isIdle() || this.naga.hasFinishedCurrentPath())) {
                Vec3d vec3d = NoPenaltyTargeting.findTo(this.naga, 4, 8, new Vec3d(this.naga.getX(), (double)(this.minY - 1), this.naga.getZ()), 1.5707963705062866D);
                if (vec3d == null) {
                    this.foundTarget = true;
                    return;
                }

                this.naga.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }

        }

        public void start() {
            this.naga.setTargetingUnderwater(true);
            this.foundTarget = false;
        }

        public void stop() {
            this.naga.setTargetingUnderwater(false);
        }
    }
}
