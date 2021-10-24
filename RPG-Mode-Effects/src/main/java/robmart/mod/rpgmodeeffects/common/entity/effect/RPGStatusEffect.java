package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RPGStatusEffect extends StatusEffect {
    private boolean needsTarget = false;

    protected RPGStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public boolean needsTarget() {
        return needsTarget;
    }

    public void setNeedsTarget(boolean needsTarget) {
        this.needsTarget = needsTarget;
    }
}
