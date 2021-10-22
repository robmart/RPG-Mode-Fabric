package robmart.mod.rpgmodecreatures.common.entity.mob;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import robmart.mod.rpgmodecreatures.common.entity.IVariants;
import robmart.mod.rpgmodecreatures.common.entity.RPGModeEntityGroup;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.Optional;

public class RatEntity extends HostileEntity implements IAnimatable, IVariants<String> {
    public static List<Optional<BiomeKeys>> biomeList = List.of(new Optional[]{
            Optional.of(BiomeKeys.PLAINS),
            Optional.of(BiomeKeys.FOREST),
            Optional.of(BiomeKeys.TAIGA),
            Optional.of(BiomeKeys.SWAMP),
            Optional.of(BiomeKeys.MUSHROOM_FIELDS),
            Optional.of(BiomeKeys.MUSHROOM_FIELD_SHORE),
            Optional.of(BiomeKeys.WOODED_HILLS),
            Optional.of(BiomeKeys.JUNGLE),
            Optional.of(BiomeKeys.JUNGLE_HILLS),
            Optional.of(BiomeKeys.JUNGLE_EDGE),
            Optional.of(BiomeKeys.BIRCH_FOREST),
            Optional.of(BiomeKeys.BIRCH_FOREST_HILLS),
            Optional.of(BiomeKeys.DARK_FOREST),
            Optional.of(BiomeKeys.WOODED_MOUNTAINS),
            Optional.of(BiomeKeys.SAVANNA),
            Optional.of(BiomeKeys.SAVANNA_PLATEAU),
            Optional.of(BiomeKeys.WOODED_BADLANDS_PLATEAU),
            Optional.of(BiomeKeys.SUNFLOWER_PLAINS),
            Optional.of(BiomeKeys.FLOWER_FOREST),
            Optional.of(BiomeKeys.SWAMP_HILLS),
            Optional.of(BiomeKeys.MODIFIED_JUNGLE),
            Optional.of(BiomeKeys.MODIFIED_JUNGLE_EDGE),
            Optional.of(BiomeKeys.TALL_BIRCH_FOREST),
            Optional.of(BiomeKeys.TALL_BIRCH_HILLS),
            Optional.of(BiomeKeys.DARK_FOREST_HILLS),
            Optional.of(BiomeKeys.GIANT_SPRUCE_TAIGA),
            Optional.of(BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS),
            Optional.of(BiomeKeys.BAMBOO_JUNGLE),
            Optional.of(BiomeKeys.BAMBOO_JUNGLE_HILLS),
    });

    public static final TrackedData<String> VARIANT = DataTracker.registerData(RatEntity.class, TrackedDataHandlerRegistry.STRING);

    private final AnimationFactory animationFactory = new AnimationFactory(this);

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
        this.targetSelector.add(counter++, new FollowTargetGoal(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createRatAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public EntityGroup getGroup() {
        return RPGModeEntityGroup.BEAST;
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return super.getEyeHeight(pose);
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rat.walk", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rat.still", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
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
}
