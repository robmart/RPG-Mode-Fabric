package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CharmStatusEffect extends RPGStatusEffect {

    protected CharmStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0xbf2271);
        this.setNeedsTarget(true);
    }
}
