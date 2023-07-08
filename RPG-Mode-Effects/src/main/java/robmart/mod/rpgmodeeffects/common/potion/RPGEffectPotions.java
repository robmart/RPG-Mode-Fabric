package robmart.mod.rpgmodeeffects.common.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import robmart.mod.rpgmodecore.common.potion.RPGPotions;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGEffectsStatusEffects;

public class RPGEffectPotions extends RPGPotions {
    public static Potion PETRIFICATION;
    public static Potion CHARM;
    public static Potion SLEEP;

    public static void initialize() {
        PETRIFICATION = of("petrification", new StatusEffectInstance(RPGEffectsStatusEffects.PETRIFICATION, 200, 0));
        CHARM = of("charm", new StatusEffectInstance(RPGEffectsStatusEffects.CHARM, 300, 0));
        SLEEP = of("sleep", new StatusEffectInstance(RPGEffectsStatusEffects.SLEEP, 800, 0));
        //TODO: Better recipes
        addRecipe(Potions.THICK, Items.DEEPSLATE, PETRIFICATION);
        addRecipe(Potions.AWKWARD, Items.ROSE_BUSH, CHARM);
        addRecipe(Potions.MUNDANE, Items.POPPY, SLEEP);
    }
}
