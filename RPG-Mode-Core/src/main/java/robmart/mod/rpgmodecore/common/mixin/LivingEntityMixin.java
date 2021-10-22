package robmart.mod.rpgmodecore.common.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
}
