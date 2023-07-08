package robmart.mod.rpgmodecore.common.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGPotions {
    protected static Potion of(String id, StatusEffectInstance statusEffect) {
        Potion potion = new Potion(statusEffect);
        Identifier identifier = new Identifier(Reference.MOD_ID, id);
        Registry.register(Registries.POTION, identifier, potion);
        return potion;
    }
}
