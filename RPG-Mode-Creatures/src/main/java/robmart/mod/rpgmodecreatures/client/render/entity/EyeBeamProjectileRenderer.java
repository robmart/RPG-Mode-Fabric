package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import robmart.mod.rpgmodecreatures.client.render.entity.model.EyeBeamProjectileModel;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EyeBeamProjectileRenderer extends GeoEntityRenderer<EyeBeamProjectile> {

    public EyeBeamProjectileRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new EyeBeamProjectileModel());
    }

    protected int getBlockLight(EyeBeamProjectile entityIn, BlockPos partialTicks) {
        return 15;
    }

//    @Override
//    public RenderLayer getRenderType(EyeBeamProjectile animatable, float partialTicks, MatrixStack stack,
//                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
//                                     Identifier textureLocation) {
//        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
//    }


    @Override
    public void actuallyRender(MatrixStack poseStack, EyeBeamProjectile animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(animatable.getYaw() - 90f));
        poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-animatable.getPitch()));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
