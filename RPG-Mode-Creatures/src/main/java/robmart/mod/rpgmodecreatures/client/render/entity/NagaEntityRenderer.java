package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import robmart.mod.rpgmodecreatures.client.render.entity.model.NagaEntityModel;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NagaEntityRenderer extends GeoEntityRenderer<NagaEntity> {
    VertexConsumerProvider vertexConsumerProvider;
    LivingEntity entity;

    public NagaEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new NagaEntityModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    protected void applyRotations(NagaEntity entityLiving, MatrixStack matrixstack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixstack, ageInTicks, rotationYaw, partialTicks);
        float i = entityLiving.getLeaningPitch(partialTicks);
        float n = entityLiving.isTouchingWater() ? -entityLiving.getPitch() : 0;
        float k = MathHelper.lerp(i, 0.0F, n);
        matrixstack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(k));
        if (entityLiving.isInSwimmingPose()) {
            matrixstack.translate(0.0D, 0, 0.30000001192092896D);
        }
    }

    @Override
    public void renderEarly(NagaEntity animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);

        vertexConsumerProvider = renderTypeBuffer;
        entity = animatable;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn,
                                  int packedOverlayIn, float red, float green, float blue, float alpha) {

        if (bone.getName().equals("Hand1")) {
            stack.push();
            //You'll need to play around with these to get item to render in the correct orientation
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(75));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));

            //You'll need to play around with this to render the item in the correct spot.
            stack.translate(0.4D, 0.47D, 1D);
            stack.translate(bone.getPositionX(), bone.getPositionZ(), bone.getPositionY());

            stack.multiply(new Quaternion(-bone.getRotationX(), -bone.getRotationZ(), bone.getRotationY(), false));

            //Sets the scaling of the item.
            stack.scale(1.0f, 1.0f, 1.0f);

            if (vertexConsumerProvider != null && entity != null)
                MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, mainHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, false, stack, vertexConsumerProvider, packedLightIn);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));


        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
