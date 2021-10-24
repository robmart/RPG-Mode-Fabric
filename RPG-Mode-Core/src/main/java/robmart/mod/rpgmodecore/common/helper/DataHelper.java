package robmart.mod.rpgmodecore.common.helper;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import robmart.mod.rpgmodecore.common.reference.Reference;

import java.util.UUID;

public class DataHelper {
    public static Entity entityFromUUID (UUID uuid) {
        if (Reference.MINECRAFT_SERVER == null) return null;

        for (ServerWorld world : Reference.MINECRAFT_SERVER.getWorlds()) {
            Entity entity = world.getEntity(uuid);
            if (entity != null) {
                return entity;
            }
        }

        return null;
    }
}
