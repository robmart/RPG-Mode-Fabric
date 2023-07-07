package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
import robmart.mod.rpgmodecore.common.reference.Reference;
import software.bernie.geckolib.model.GeoModel;

public class EyeBeamProjectileModel extends GeoModel<EyeBeamProjectile> {
    @Override
    public Identifier getModelResource(EyeBeamProjectile object) {
        return new Identifier(Reference.MOD_ID, "geo/eye_beam.geo.json");
    }

    @Override
    public Identifier getTextureResource(EyeBeamProjectile object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/eye_beam/eye_beam_" + object.getVariant() + ".png");
    }

    @Override
    public Identifier getAnimationResource(EyeBeamProjectile animatable) {
        return null;
    }
}
