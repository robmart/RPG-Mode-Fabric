package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecreatures.common.entity.mob.boss.BeholderEntity;
import robmart.mod.rpgmodecreatures.common.reference.Reference;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BeholderEntityModel extends AnimatedGeoModel<BeholderEntity> {
    @Override
    public Identifier getModelLocation(BeholderEntity object) {
        return new Identifier(Reference.MOD_ID, "geo/beholder.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BeholderEntity object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/beholder/beholder.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BeholderEntity animatable) {
        return null;
    }
}
