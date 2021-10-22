package robmart.mod.rpgmodecreatures.common.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import robmart.mod.rpgmodecreatures.common.RPGModeCreatures;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;

@Mixin(SpawnRestriction.class)
public class SpawnRestrictionMixin {

    @Shadow
    private static <T extends MobEntity> void register(EntityType<T> type, SpawnRestriction.Location location,
                                                       Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
    }

    static {
        register(RPGModeCreatures.NAGA, SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NagaEntity::canSpawn);
    }
}