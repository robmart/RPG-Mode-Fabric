package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGStatusEffects {
    public static final StatusEffect PETRIFICATION = new PetrificationStatusEffect();
    public static final StatusEffect CHARM = new CharmStatusEffect();

    public static void register() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "petrification"), PETRIFICATION);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "charm"), CHARM);
    }
}
