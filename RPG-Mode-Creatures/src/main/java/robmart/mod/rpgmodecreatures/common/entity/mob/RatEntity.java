package robmart.mod.rpgmodecreatures.common.entity.mob;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import robmart.mod.rpgmodecreatures.common.sound.RPGCreaturesSounds;
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

import java.util.List;
import java.util.Optional;

public class RatEntity extends HostileEntity implements GeoAnimatable, IVariants<String> {
    public static List<Optional<BiomeKeys>> biomeList = List.of(new Optional[]{
            Optional.of(BiomeKeys.PLAINS),
            Optional.of(BiomeKeys.FOREST),
            Optional.of(BiomeKeys.TAIGA),
            Optional.of(BiomeKeys.SWAMP),
            Optional.of(BiomeKeys.MUSHROOM_FIELDS),
            Optional.of(BiomeKeys.JUNGLE),
            Optional.of(BiomeKeys.SPARSE_JUNGLE),
            Optional.of(BiomeKeys.BIRCH_FOREST),
            Optional.of(BiomeKeys.DARK_FOREST),
            Optional.of(BiomeKeys.SAVANNA),
            Optional.of(BiomeKeys.SAVANNA_PLATEAU),
            Optional.of(BiomeKeys.WOODED_BADLANDS),
            Optional.of(BiomeKeys.SUNFLOWER_PLAINS),
            Optional.of(BiomeKeys.FLOWER_FOREST),
            Optional.of(BiomeKeys.MANGROVE_SWAMP),
            Optional.of(BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
            Optional.of(BiomeKeys.OLD_GROWTH_PINE_TAIGA),
            Optional.of(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA),
            Optional.of(BiomeKeys.BAMBOO_JUNGLE),
            Optional.of(BiomeKeys.WINDSWEPT_HILLS),
            Optional.of(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS),
            Optional.of(BiomeKeys.DESERT),
            Optional.of(BiomeKeys.WINDSWEPT_FOREST),
            Optional.of(BiomeKeys.WINDSWEPT_SAVANNA),
            Optional.of(BiomeKeys.BADLANDS),
            Optional.of(BiomeKeys.ERODED_BADLANDS),
            Optional.of(BiomeKeys.MEADOW),
            Optional.of(BiomeKeys.BEACH),
            Optional.of(BiomeKeys.STONY_SHORE),
    });

    public static final TrackedData<String> VARIANT = DataTracker.registerData(RatEntity.class, TrackedDataHandlerRegistry.STRING);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation WALK_ANIM = RawAnimation.begin().thenPlay("animation.rat.walk");
    public static final RawAnimation STILL_ANIM = RawAnimation.begin().thenPlay("animation.rat.still");

    private int tickCounter = 0;

    public RatEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        int counter = 0;
        this.goalSelector.add(counter++, new SwimGoal(this));
        this.goalSelector.add(counter++, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(counter++, new WanderAroundGoal(this, 1D));
        this.goalSelector.add(counter, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(counter++, new LookAroundGoal(this));

        counter = 1;
        this.targetSelector.add(counter++, (new RevengeGoal(this)).setGroupRevenge());
        this.targetSelector.add(counter++, new ActiveTargetGoal(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createRatAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
    }


    @Override
    public EntityGroup getGroup() {
        return RPGEntityGroup.BEAST;
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isClient)
            tickCounter++;
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return super.getEyeHeight(pose);
    }

    private PlayState animationPredicate(AnimationState<RatEntity> animationState) {
        if (animationState.isMoving()) {
            animationState.getController().setAnimation(WALK_ANIM);
        } else {
            animationState.getController().setAnimation(STILL_ANIM);
        }
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

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, "black");
    }

    @Override
    public EntityData initialize(ServerWorldAccess serverWorldAccess, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, EntityData entityData, NbtCompound entityTag) {
        int rand = this.random.nextInt(3);
        this.setVariant(rand == 0 ? "black" : rand == 1 ? "brown" : "gray");
        return super.initialize(serverWorldAccess, difficulty, spawnReason, entityData, entityTag);
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

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return RPGCreaturesSounds.RAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return RPGCreaturesSounds.RAT_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return RPGCreaturesSounds.RAT_AMBIENT;
    }
}
