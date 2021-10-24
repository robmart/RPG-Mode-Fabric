package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.effect.StatusEffectCategory;

public class CharmStatusEffect extends RPGStatusEffect {

    protected CharmStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbf2271);
        this.setNeedsTarget(true);
    }
}
