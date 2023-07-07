package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import robmart.mod.rpgmodecreatures.client.render.entity.model.EyeBeamProjectileModel;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
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
}
