package robmart.mod.rpgmodecreatures.common.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import robmart.mod.rpgmodecore.common.entity.RPGEntityTypes;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodecreatures.common.entity.mob.RatEntity;
import robmart.mod.rpgmodecreatures.common.entity.mob.boss.BeholderEntity;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;

import java.util.Optional;

public class RPGCreaturesEntityTypes extends RPGEntityTypes {


    public static EntityType<RatEntity> RAT;
    public static EntityType<NagaEntity> NAGA;
    public static EntityType<BeholderEntity> BEHOLDER;
    public static EntityType<EyeBeamProjectile> BEHOLDER_EYE_BEAM;

    public static void initialize() {
        RAT = of("rat", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RatEntity::new)
                .dimensions(EntityDimensions.fixed(0.65f, 0.45f))
                .build());
        NAGA = of("naga", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NagaEntity::new)
                .dimensions(EntityDimensions.changing(0.75f, 2.25f))
                .build());
        BEHOLDER = of("beholder", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BeholderEntity::new)
                .dimensions(EntityDimensions.changing(2.2f, 2.2f))
                .build());
        BEHOLDER_EYE_BEAM = of("beholder_eye_beam", FabricEntityTypeBuilder.<EyeBeamProjectile>create(SpawnGroup.MISC, EyeBeamProjectile::new)
                .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                .trackRangeBlocks(4)
                .trackedUpdateRate(20)
                .build());

        FabricDefaultAttributeRegistry.register(RPGCreaturesEntityTypes.RAT, RatEntity.createRatAttributes());
        FabricDefaultAttributeRegistry.register(RPGCreaturesEntityTypes.NAGA, NagaEntity.createNagaAttributes());
        FabricDefaultAttributeRegistry.register(RPGCreaturesEntityTypes.BEHOLDER, BeholderEntity.createBeholderAttributes());

        BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> RatEntity.biomeList.contains(Optional.of(context.getBiomeKey()))),
                SpawnGroup.MONSTER, RPGCreaturesEntityTypes.RAT, 50, 2, 6);

        BiomeModifications.addSpawn(BiomeSelectors.all().and(context -> NagaEntity.biomeList.contains(Optional.of(context.getBiomeKey()))),
                SpawnGroup.MONSTER, RPGCreaturesEntityTypes.NAGA, 25, 1, 2);
    }
}
