package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecreatures.common.entity.mob.boss.BeholderEntity;
import robmart.mod.rpgmodecore.common.reference.Reference;
import software.bernie.geckolib.model.GeoModel;

public class BeholderEntityModel extends GeoModel<BeholderEntity> {
    @Override
    public Identifier getModelResource(BeholderEntity object) {
        return new Identifier(Reference.MOD_ID, "geo/beholder.geo.json");
    }

    @Override
    public Identifier getTextureResource(BeholderEntity object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/beholder/beholder.png");
    }

    @Override
    public Identifier getAnimationResource(BeholderEntity animatable) {
        return null;
    }
}
