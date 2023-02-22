package robmart.mod.rpgmodecreatures.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodecreatures.common.entity.mob.RatEntity;
import robmart.mod.rpgmodecreatures.common.entity.mob.boss.BeholderEntity;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
import robmart.mod.rpgmodecreatures.common.reference.Reference;
import software.bernie.geckolib.GeckoLib;

import java.util.Optional;


public class RPGModeCreatures implements ModInitializer {
    public static final EntityType<RatEntity> RAT = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Reference.MOD_ID, "rat"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RatEntity::new)
                    .dimensions(EntityDimensions.fixed(0.65f, 0.45f))
                    .build()
    );
    public static final EntityType<NagaEntity> NAGA = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Reference.MOD_ID, "naga"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NagaEntity::new)
                    .dimensions(EntityDimensions.changing(0.75f, 2.25f))
                    .build()
    );
    public static final EntityType<BeholderEntity> BEHOLDER = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Reference.MOD_ID, "beholder"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BeholderEntity::new)
                    .dimensions(EntityDimensions.changing(2.2f, 2.2f))
                    .build()
    );
    public static final EntityType<EyeBeamProjectile> BEHOLDER_EYE_BEAM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Reference.MOD_ID, "beholder_eye_beam"),
            FabricEntityTypeBuilder.<EyeBeamProjectile>create(SpawnGroup.MISC, EyeBeamProjectile::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        FabricDefaultAttributeRegistry.register(RAT, RatEntity.createRatAttributes());
        FabricDefaultAttributeRegistry.register(NAGA, NagaEntity.createNagaAttributes());
        FabricDefaultAttributeRegistry.register(BEHOLDER, BeholderEntity.createBeholderAttributes());

        BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> RatEntity.biomeList.contains(Optional.of(context.getBiomeKey()))),
                SpawnGroup.MONSTER, RAT, 50, 2, 6);

        BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> NagaEntity.biomeList.contains(Optional.of(context.getBiomeKey()))),
                SpawnGroup.MONSTER, NAGA, 25, 1, 2);
    }
}
