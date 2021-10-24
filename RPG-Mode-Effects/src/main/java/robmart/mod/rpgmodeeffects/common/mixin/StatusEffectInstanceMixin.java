package robmart.mod.rpgmodeeffects.common.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robmart.mod.rpgmodecore.common.helper.DataHelper;
import robmart.mod.rpgmodeeffects.common.entity.effect.IStatusEffectTarget;
import robmart.mod.rpgmodeeffects.common.entity.effect.RPGStatusEffect;

import java.util.UUID;

@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin implements IStatusEffectTarget {
    private Entity attacker;
    private Entity target;
    private UUID attackerUUID = null;
    private UUID targetUUID = null;

    //TODO: Use inject instead, for compatibility
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;fromNbt(Lnet/minecraft/entity/effect/StatusEffect;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;"), method = "fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/effect/StatusEffectInstance;")
    private static StatusEffectInstance fromNbt(StatusEffect type, NbtCompound nbt) {
        int i = nbt.getByte("Amplifier");
        int j = nbt.getInt("Duration");
        boolean bl = nbt.getBoolean("Ambient");
        boolean bl2 = true;
        if (nbt.contains("ShowParticles", 1)) {
            bl2 = nbt.getBoolean("ShowParticles");
        }

        boolean bl3 = bl2;
        if (nbt.contains("ShowIcon", 1)) {
            bl3 = nbt.getBoolean("ShowIcon");
        }

        StatusEffectInstance statusEffectInstance = null;
        if (nbt.contains("HiddenEffect", 10)) {
            statusEffectInstance = fromNbt(type, nbt.getCompound("HiddenEffect"));
        }

        if (type instanceof RPGStatusEffect rpgStatusEffect && rpgStatusEffect.needsTarget()) {
            StatusEffectInstance instance = new StatusEffectInstance(type, j, Math.max(i, 0), bl, bl2, bl3, statusEffectInstance);

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

            return instance;
        }

        return new StatusEffectInstance(type, j, Math.max(i, 0), bl, bl2, bl3, statusEffectInstance);
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
