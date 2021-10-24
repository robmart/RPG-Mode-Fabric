package robmart.mod.rpgmodeeffects.common.entity.effect;

import net.minecraft.entity.Entity;

public interface IStatusEffectTarget {
    Entity getAttacker();
    void setAttacker(Entity attacker);
    Entity getTarget();
    void setTarget(Entity target);
}
