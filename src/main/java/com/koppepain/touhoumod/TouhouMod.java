package com.koppepain.touhoumod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koppepain.touhoumod.entity.YukkuriEntity;
import com.koppepain.touhoumod.registry.ModEntities;
import com.koppepain.touhoumod.registry.ModItems;

public class TouhouMod implements ModInitializer {
	public static final String MOD_ID = "touhoumod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[touhoumod] initializing yukkuri & danmaku systems");

		ModEntities.register();
		ModItems.register();

		FabricDefaultAttributeRegistry.register(ModEntities.YUKKURI, YukkuriEntity.createYukkuriAttributes());
	}
}
