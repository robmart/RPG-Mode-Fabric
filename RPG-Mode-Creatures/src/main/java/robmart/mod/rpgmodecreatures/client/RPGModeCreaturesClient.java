package robmart.mod.rpgmodecreatures.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import robmart.mod.rpgmodecreatures.client.render.entity.BeholderEntityRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.EyeBeamProjectileRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.NagaEntityRenderer;
import robmart.mod.rpgmodecreatures.client.render.entity.RatEntityRenderer;
import robmart.mod.rpgmodecreatures.common.entity.RPGCreaturesEntityTypes;

@Environment(EnvType.CLIENT)
public class RPGModeCreaturesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(RPGCreaturesEntityTypes.RAT, RatEntityRenderer::new);
        EntityRendererRegistry.register(RPGCreaturesEntityTypes.NAGA, NagaEntityRenderer::new);
        EntityRendererRegistry.register(RPGCreaturesEntityTypes.BEHOLDER, BeholderEntityRenderer::new);

        EntityRendererRegistry.register(RPGCreaturesEntityTypes.BEHOLDER_EYE_BEAM, EyeBeamProjectileRenderer::new);
    }
}
