package com.koppepain.touhoumod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import com.koppepain.touhoumod.client.render.DanmakuBulletEntityRenderer;
import com.koppepain.touhoumod.client.render.TouhouShaders;
import com.koppepain.touhoumod.client.render.YukkuriEntityModel;
import com.koppepain.touhoumod.client.render.YukkuriEntityRenderer;
import com.koppepain.touhoumod.registry.ModEntities;

public class TouhouModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		TouhouShaders.register();

		EntityModelLayerRegistry.registerModelLayer(YukkuriEntityModel.LAYER, YukkuriEntityModel::getTexturedModelData);

		EntityRendererRegistry.register(ModEntities.YUKKURI, YukkuriEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.DANMAKU_BULLET, DanmakuBulletEntityRenderer::new);
	}
}
