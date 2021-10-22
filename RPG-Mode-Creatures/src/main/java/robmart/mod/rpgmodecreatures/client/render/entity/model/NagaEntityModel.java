package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodecore.common.reference.Reference;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class NagaEntityModel extends AnimatedGeoModel<NagaEntity> {

    @Override
    public void setLivingAnimations(NagaEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone arm1 = this.getAnimationProcessor().getBone("Arm1");
        IBone arm2 = this.getAnimationProcessor().getBone("Arm2");

        if (customPredicate != null) {
            //noinspection unchecked
            EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
            head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
            head.setRotationX(-extraData.headPitch * ((float)Math.PI / 180F));

            if (customPredicate.getController() != null && customPredicate.getController().getCurrentAnimation() != null && !customPredicate.getController().getCurrentAnimation().animationName.equals("animation.naga.bow")) {

                float limbSwingAmount = customPredicate.getLimbSwingAmount();
                float limbSwing = customPredicate.getLimbSwing();

                float modLimbSwingAmount = limbSwingAmount > 0.2F ? limbSwingAmount * 0.4F : limbSwingAmount;
                arm1.setRotationX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * modLimbSwingAmount);
                arm2.setRotationX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * modLimbSwingAmount);
            }
        }

    }

    @Override
    public Identifier getModelLocation(NagaEntity object) {
        return new Identifier(Reference.MOD_ID, "geo/naga.geo.json");
    }

    @Override
    public Identifier getTextureLocation(NagaEntity object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/naga/naga_" + object.getVariant() + ".png");
    }

    @Override
    public Identifier getAnimationFileLocation(NagaEntity animatable) {
        return new Identifier(Reference.MOD_ID, "animations/naga.animation.json");
    }
}
