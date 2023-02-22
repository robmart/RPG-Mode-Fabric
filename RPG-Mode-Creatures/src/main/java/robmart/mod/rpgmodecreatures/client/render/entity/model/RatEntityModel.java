// Made with Blockbench 3.9.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package robmart.mod.rpgmodecreatures.client.render.entity.model;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import robmart.mod.rpgmodecreatures.common.entity.mob.RatEntity;
import robmart.mod.rpgmodecreatures.common.reference.Reference;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RatEntityModel extends GeoModel<RatEntity> {

	@Override
	public void setCustomAnimations(RatEntity entity, long uniqueID, AnimationState<RatEntity> customPredicate) {
		super.setCustomAnimations(entity, uniqueID, customPredicate);
		CoreGeoBone head = this.getAnimationProcessor().getBone("Head");

		EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		head.setRotY(MathHelper.clamp(extraData.netHeadYaw() * ((float) Math.PI / 180F), -0.8f, 0.8f));
	}

	@Override
	public Identifier getModelResource(RatEntity object) {
		return new Identifier(Reference.MOD_ID, "geo/rat.geo.json");
	}

	@Override
	public Identifier getTextureResource(RatEntity object) {
		return new Identifier(Reference.MOD_ID, "textures/entity/rat/rat_" + object.getVariant() + ".png");
	}

	@Override
	public Identifier getAnimationResource(RatEntity animatable) {
		return new Identifier(Reference.MOD_ID, "animations/rat.animation.json");
	}
}