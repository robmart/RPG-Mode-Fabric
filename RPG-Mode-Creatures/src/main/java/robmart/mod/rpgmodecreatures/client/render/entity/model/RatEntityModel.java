// Made with Blockbench 3.9.3
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports

package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import robmart.mod.rpgmodecreatures.common.entity.mob.NagaEntity;
import robmart.mod.rpgmodecreatures.common.entity.mob.RatEntity;
import robmart.mod.rpgmodecreatures.common.reference.Reference;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RatEntityModel extends AnimatedGeoModel<RatEntity> {

	@Override
	public void setLivingAnimations(RatEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("Head");
		if (customPredicate != null) {
			//noinspection unchecked
			EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
			head.setRotationY(MathHelper.clamp(extraData.netHeadYaw * ((float) Math.PI / 180F), -0.8f, 0.8f));
		}
	}

	@Override
	public Identifier getModelLocation(RatEntity object) {
		return new Identifier(Reference.MOD_ID, "geo/rat.geo.json");
	}

	@Override
	public Identifier getTextureLocation(RatEntity object) {
		return new Identifier(Reference.MOD_ID, "textures/entity/rat/rat_" + object.getVariant() + ".png");
	}

	@Override
	public Identifier getAnimationFileLocation(RatEntity animatable) {
		return new Identifier(Reference.MOD_ID, "animations/rat.animation.json");
	}
}