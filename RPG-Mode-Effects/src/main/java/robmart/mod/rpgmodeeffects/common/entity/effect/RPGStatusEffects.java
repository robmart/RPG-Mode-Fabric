package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGStatusEffects {
    public static final RPGStatusEffect PETRIFICATION = new PetrificationStatusEffect();
    public static final RPGStatusEffect CHARM = new CharmStatusEffect();
    public static final RPGStatusEffect SLEEP = new SleepStatusEffect();

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "petrification"), PETRIFICATION);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "charm"), CHARM);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(Reference.MOD_ID, "sleep"), SLEEP);
    }
}
