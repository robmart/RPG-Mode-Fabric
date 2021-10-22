package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import robmart.mod.rpgmodecreatures.client.render.entity.model.BeholderEntityModel;
import robmart.mod.rpgmodecreatures.common.entity.mob.boss.BeholderEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BeholderEntityRenderer extends GeoEntityRenderer<BeholderEntity> {

    public BeholderEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BeholderEntityModel());
    }

    @Override
    public void render(BeholderEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        stack.scale(2, 2, 2);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.scale(0.5f, 0.5f, 0.5f);
    }
}
