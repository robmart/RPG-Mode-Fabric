package robmart.mod.rpgmodecreatures.common.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecore.common.reference.Reference;

public class RPGModeCreaturesSounds {
    public static SoundEvent RAT_HURT;
    public static SoundEvent RAT_DEATH;
    public static SoundEvent RAT_AMBIENT;

    public static SoundEvent NAGA_HURT;
    public static SoundEvent NAGA_DEATH;
    public static SoundEvent NAGA_AMBIENT;

    static SoundEvent of(String id) {
        Identifier identifier = new Identifier(Reference.MOD_ID, id);
        SoundEvent sound = SoundEvent.of(identifier);
        Registry.register(Registries.SOUND_EVENT, identifier, sound);
        return sound;
    }

    public static void initialize() {
        RAT_HURT = of("rpgmode.rat.hurt");
        RAT_DEATH = of("rpgmode.rat.death");
        RAT_AMBIENT = of("rpgmode.rat.ambient");

        NAGA_HURT = of("rpgmode.naga.hurt");
        NAGA_DEATH = of("rpgmode.naga.death");
        NAGA_AMBIENT = of("rpgmode.naga.ambient");
    }
}
