package robmart.mod.rpgmodecreatures.common.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import robmart.mod.rpgmodecreatures.common.entity.RPGCreaturesEntityTypes;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;

@Mixin(SpawnRestriction.class)
public class SpawnRestrictionMixin {

    @Shadow
    public static <T extends MobEntity> void register(EntityType<T> type, SpawnRestriction.Location location,
                                                      Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate) {
    }

    static {
        register(RPGCreaturesEntityTypes.NAGA, SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NagaEntity::canSpawn);
    }
}