package robmart.mod.rpgmodecreatures.client.render.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import robmart.mod.rpgmodecreatures.client.render.entity.model.RatEntityModel;
import robmart.mod.rpgmodecreatures.common.entity.mob.RatEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RatEntityRenderer extends GeoEntityRenderer<RatEntity> {

    public RatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RatEntityModel());
        this.shadowRadius = 0.5F;
    }
}
