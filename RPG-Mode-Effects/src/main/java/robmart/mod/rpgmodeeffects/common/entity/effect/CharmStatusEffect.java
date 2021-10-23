package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CharmStatusEffect extends StatusEffect {
    private Entity attacker;
    private Entity target;

    protected CharmStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbf2271);
    }

    public Entity getAttacker() {
        return attacker;
    }

    public void setAttacker(Entity attacker) {
        this.attacker = attacker;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
