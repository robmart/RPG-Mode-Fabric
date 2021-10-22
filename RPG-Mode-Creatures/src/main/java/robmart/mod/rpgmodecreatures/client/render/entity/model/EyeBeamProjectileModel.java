package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import robmart.mod.rpgmodecreatures.common.entity.projectile.EyeBeamProjectile;
import robmart.mod.rpgmodecore.common.reference.Reference;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EyeBeamProjectileModel extends AnimatedGeoModel<EyeBeamProjectile> {
    @Override
    public Identifier getModelLocation(EyeBeamProjectile object) {
        return new Identifier(Reference.MOD_ID, "geo/eye_beam.geo.json");
    }

    @Override
    public Identifier getTextureLocation(EyeBeamProjectile object) {
        return new Identifier(Reference.MOD_ID, "textures/entity/eye_beam.png");
    }

    @Override
    public Identifier getAnimationFileLocation(EyeBeamProjectile animatable) {
        return null;
    }
}
