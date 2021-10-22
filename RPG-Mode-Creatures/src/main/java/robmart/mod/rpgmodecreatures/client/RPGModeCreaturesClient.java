package robmart.mod.rpgmodecreatures.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import robmart.mod.rpgmodecreatures.client.render.entity.BeholderEntityRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.EyeBeamProjectileRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.NagaEntityRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.RatEntityRenderer;
import robmart.mod.rpgmodecreatures.common.RPGModeCreatures;

@Environment(EnvType.CLIENT)
public class RPGModeCreaturesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(RPGModeCreatures.RAT, RatEntityRenderer::new);
        EntityRendererRegistry.register(RPGModeCreatures.NAGA, NagaEntityRenderer::new);
        EntityRendererRegistry.register(RPGModeCreatures.BEHOLDER, BeholderEntityRenderer::new);

        EntityRendererRegistry.register(RPGModeCreatures.BEHOLDER_EYE_BEAM, EyeBeamProjectileRenderer::new);
    }
}
