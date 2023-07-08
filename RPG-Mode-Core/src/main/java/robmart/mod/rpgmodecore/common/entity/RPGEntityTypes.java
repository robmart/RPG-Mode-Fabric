package robmart.mod.rpgmodecore.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGEntityTypes {
    protected static EntityType of(String id, EntityType entity) {
        Identifier identifier = new Identifier(Reference.MOD_ID, id);
        return Registry.register(Registries.ENTITY_TYPE, identifier, entity);
    }
}
