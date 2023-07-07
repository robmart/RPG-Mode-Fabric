package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import robmart.mod.rpgmodecore.common.reference.Reference;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NagaEntityModel extends GeoModel<NagaEntity> {

    @Override
    public void setCustomAnimations(NagaEntity entity, long uniqueID, @Nullable AnimationState<NagaEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone head = this.getAnimationProcessor().getBone("Head");
        CoreGeoBone arm1 = this.getAnimationProcessor().getBone("Arm1");
        CoreGeoBone arm2 = this.getAnimationProcessor().getBone("Arm2");

        if (customPredicate != null) {
            EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotY(extraData.netHeadYaw() * ((float)Math.PI / 180F));
            head.setRotX(-extraData.headPitch() * ((float)Math.PI / 180F));

            if (customPredicate.getController() != null && customPredicate.getController().getCurrentAnimation() != null && !customPredicate.getController().getCurrentRawAnimation().equals(NagaEntity.BOW_ANIM)) {

                float limbSwingAmount = customPredicate.getLimbSwingAmount();
                float limbSwing = customPredicate.getLimbSwing();

                float modLimbSwingAmount = limbSwingAmount > 0.2F ? limbSwingAmount * 0.4F : limbSwingAmount;
                arm1.setRotX(MathHelper.cos(limbSwing * 0.6662F) * 1.4F * modLimbSwingAmount);
                arm2.setRotX(MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * modLimbSwingAmount);
            }
        }

    }

    @Override
    public Identifier getModelResource(NagaEntity object) {
        return new Identifier(Reference.MOD_ID, "geo/naga.geo.json");
    }

    @Override
    public Identifier getTextureResource(NagaEntity object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/naga/naga_" + object.getVariant() + ".png");
    }

    @Override
    public Identifier getAnimationResource(NagaEntity animatable) {
        return new Identifier(Reference.MOD_ID, "animations/naga.animation.json");
    }
}
