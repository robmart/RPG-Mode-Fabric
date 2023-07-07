package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.math.BlockPos;
import robmart.mod.rpgmodecreatures.client.render.entity.model.EyeBeamProjectileModel;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;

public class EyeBeamProjectileRenderer extends GeoProjectileRenderer<EyeBeamProjectile> {

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
}
