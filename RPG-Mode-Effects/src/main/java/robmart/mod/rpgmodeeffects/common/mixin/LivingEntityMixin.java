package robmart.mod.rpgmodeeffects.common.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import robmart.mod.rpgmodeeffects.common.entity.effect.IStatusEffectTarget;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At(value = "TAIL"), method = "onStatusEffectApplied")
    private void onStatusEffectApplied(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        if (!this.world.isClient && effect.getEffectType() instanceof RPGStatusEffect rpgStatusEffect && rpgStatusEffect.needsTarget()) {
            ((IStatusEffectTarget) effect).setTarget(source);
            ((IStatusEffectTarget) effect).setAttacker((LivingEntity) (Object) this);
        }
    }
}
