package robmart.mod.rpgmodecore.common.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGStatusEffects {
    protected static StatusEffect of(String id, StatusEffect effect) {
        Identifier identifier = new Identifier(Reference.MOD_ID, id);
        Registry.register(Registries.STATUS_EFFECT, identifier, effect);
        return effect;
    }
}
