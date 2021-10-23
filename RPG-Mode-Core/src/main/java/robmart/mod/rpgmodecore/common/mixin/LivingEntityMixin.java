package robmart.mod.rpgmodecore.common.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robmart.mod.rpgmodecore.api.event.LivingEntityEvents;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At(value = "TAIL"), method = "jump()V")
    private void jump(CallbackInfo ci) {
        ActionResult result = LivingEntityEvents.JUMPING_EVENT.invoker().onJump((LivingEntity) (Object) this);

        if(result == ActionResult.FAIL) {
            this.setVelocity(0, 0, 0);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z"), method = "damage", cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = LivingEntityEvents.DAMAGE_EVENT.invoker().onDamage((LivingEntity) (Object) this, source, amount);

        if(result == ActionResult.FAIL) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
