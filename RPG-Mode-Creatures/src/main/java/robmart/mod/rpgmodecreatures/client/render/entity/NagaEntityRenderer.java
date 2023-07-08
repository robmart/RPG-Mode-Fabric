package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import robmart.mod.rpgmodecreatures.client.render.entity.model.NagaEntityModel;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

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
        matrixstack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(k));
        if (entityLiving.isInSwimmingPose()) {
            matrixstack.translate(0.0D, 0, 0.30000001192092896D);
        }
    }

    @Override
    public void preRender(MatrixStack poseStack, NagaEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource,
                          VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight,
                packedOverlay, red, green, blue, alpha);

        vertexConsumerProvider = bufferSource;
        entity = animatable;
    }

    @Override
    public void renderRecursively(MatrixStack stack, NagaEntity animatable, GeoBone bone, RenderLayer renderType,
                                  VertexConsumerProvider bufferSource, VertexConsumer bufferIn, boolean isReRender,
                                  float partialTick, int packedLightIn, int packedOverlayIn, float red, float green,
                                  float blue, float alpha) {

        if (bone.getName().equals("Hand1")) {
            boolean bl = ((LivingEntity)animatable).getMainArm() == Arm.RIGHT;
            ItemStack itemStack2 = bl ? animatable.getMainHandStack() : animatable.getOffHandStack();
            if (!itemStack2.isEmpty()) {
                stack.push();
                if (vertexConsumerProvider != null && entity != null) {
                    this.renderItem(bone, animatable, itemStack2, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, stack, vertexConsumerProvider, packedLightIn);
                }
                stack.pop();
                bufferSource.getBuffer(RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable)));
            }
        } else if (bone.getName().equals("Hand2")) {
            boolean bl = ((LivingEntity)animatable).getMainArm() == Arm.RIGHT;
            ItemStack itemStack = bl ? animatable.getOffHandStack() : animatable.getMainHandStack();
            if (!itemStack.isEmpty()) {
                stack.push();
                if (vertexConsumerProvider != null && entity != null) {
                    this.renderItem(bone, animatable, itemStack, ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, stack, vertexConsumerProvider, packedLightIn);
                }
                stack.pop();
                bufferSource.getBuffer(RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable)));
            }
        }

        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, bufferIn, isReRender, partialTick,
                packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    protected void renderItem(GeoBone bone, LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!stack.isEmpty()) {
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            matrices.translate(0.475D, 0.1D, 1.05D);
            if (!stack.getItem().equals(Items.BOW))
                matrices.translate(-0.1D, 0D, 0D);

            matrices.translate(bone.getPosX(), bone.getPosZ(), bone.getPosY());
            matrices.multiply(new Quaternionf(-bone.getRotX(), -bone.getRotZ(), bone.getRotY(), 1));

            boolean bl = arm == Arm.LEFT;
            matrices.translate((float)(bl ? -0.95 : 0), 0, (bl ? 0.025 : 0));
            if (!stack.getItem().equals(Items.BOW))
                matrices.translate((float)(bl ? 0.2 : 0), 0, (bl ? 0.025 : 0));
            MinecraftClient.getInstance().gameRenderer.firstPersonRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }
}
