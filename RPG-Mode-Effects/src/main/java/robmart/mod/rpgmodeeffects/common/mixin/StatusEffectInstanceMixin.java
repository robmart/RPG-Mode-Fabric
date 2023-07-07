package robmart.mod.rpgmodeeffects.common.mixin;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import robmart.mod.rpgmodecore.common.helper.DataHelper;
import robmart.mod.rpgmodeeffects.common.entity.effect.IStatusEffectTarget;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffect;

import java.util.Optional;
import java.util.UUID;

@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin implements IStatusEffectTarget {
    @Unique
    private Entity attacker;
    @Unique
    private Entity target;
    @Unique
    private UUID attackerUUID = null;
    @Unique
    private UUID targetUUID = null;

    @Inject(at = @At(value = "TAIL"), method = "fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void fromNbt(StatusEffect type, NbtCompound nbt, CallbackInfoReturnable<StatusEffectInstance> cir, int i, int j, boolean bl, boolean bl2, boolean bl3, StatusEffectInstance statusEffectInstance, Optional optional) {
        if (type instanceof RPGStatusEffect rpgStatusEffect && rpgStatusEffect.needsTarget()) {
            StatusEffectInstance instance = new StatusEffectInstance(type, j, Math.max(i, 0), bl, bl2, bl3, statusEffectInstance, optional);

            if (nbt.containsUuid("AttackerUUID")) {
                Entity attacker = DataHelper.entityFromUUID(nbt.getUuid("AttackerUUID"));

                if (attacker != null) {
                    ((StatusEffectInstanceMixin) (Object) instance).setAttacker(attacker);
                } else if (((StatusEffectInstanceMixin) (Object) instance).attackerUUID == null)  {
                    ((StatusEffectInstanceMixin) (Object) instance).attackerUUID = nbt.getUuid("AttackerUUID");
                }
            }

            if (nbt.containsUuid("TargetUUID")) {
                Entity target = DataHelper.entityFromUUID(nbt.getUuid("TargetUUID"));

                if (target != null) {
                    ((StatusEffectInstanceMixin) (Object) instance).setTarget(target);
                } else if (((StatusEffectInstanceMixin) (Object) instance).targetUUID == null) {
                    ((StatusEffectInstanceMixin) (Object) instance).targetUUID = nbt.getUuid("TargetUUID");
                }
            }

            cir.setReturnValue(instance);
        }
    }

    @Inject(method = "update", at = @At("TAIL"))
    public void update(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (this.attackerUUID != null && getAttacker() == null) {
            Entity attacker = DataHelper.entityFromUUID(this.attackerUUID);

            if (attacker != null) {
                this.setAttacker(attacker);
            }
        }

        if (this.targetUUID != null && getTarget() == null) {
            Entity target = DataHelper.entityFromUUID(this.targetUUID);

            if (target != null) {
                this.setTarget(target);
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (getAttacker() != null) {
            nbt.putUuid("AttackerUUID", getAttacker().getUuid());
        } else if (this.attackerUUID != null) {
            nbt.putUuid("AttackerUUID", this.attackerUUID);
        }

        if (getTarget() != null) {
            nbt.putUuid("TargetUUID", getTarget().getUuid());
        } else if (this.targetUUID != null) {
            nbt.putUuid("TargetUUID", this.targetUUID);
        }
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
