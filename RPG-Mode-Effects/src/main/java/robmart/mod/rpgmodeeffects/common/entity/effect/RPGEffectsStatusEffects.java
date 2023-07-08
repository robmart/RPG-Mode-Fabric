package robmart.mod.rpgmodeeffects.common.entity.effect;

import robmart.mod.rpgmodecore.common.entity.effect.RPGStatusEffects;

public class RPGEffectsStatusEffects extends RPGStatusEffects {
    public static RPGStatusEffect PETRIFICATION = new PetrificationStatusEffect();
    public static RPGStatusEffect CHARM = new CharmStatusEffect();
    public static RPGStatusEffect SLEEP = new SleepStatusEffect();

    public static void initialize() {
        PETRIFICATION = (RPGStatusEffect) of("petrification", new PetrificationStatusEffect());
        CHARM = (RPGStatusEffect) of("charm", new CharmStatusEffect());
        SLEEP = (RPGStatusEffect) of("sleep", new SleepStatusEffect());
    }
}
