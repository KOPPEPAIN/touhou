package com.koppepain.touhoumod.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.DanmakuBulletEntity;
import com.koppepain.touhoumod.entity.YukkuriEntity;

/**
 * Entity type registration.
 */
public final class ModEntities {
	public static final EntityType<YukkuriEntity> YUKKURI = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(TouhouMod.MOD_ID, "yukkuri"),
			EntityType.Builder.create(YukkuriEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.9f, 0.6f)
					.maxTrackingRange(48)
					.trackingTickInterval(3)
					.build("yukkuri"));

	public static final EntityType<DanmakuBulletEntity> DANMAKU_BULLET = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of(TouhouMod.MOD_ID, "danmaku_bullet"),
			EntityType.Builder.<DanmakuBulletEntity>create(DanmakuBulletEntity::new, SpawnGroup.MISC)
					.dimensions(0.25f, 0.25f)
					.maxTrackingRange(64)
					.trackingTickInterval(1)
					.build("danmaku_bullet"));

	private ModEntities() {
	}

	/**
	 * Forces this class to load (and thus run the static registrations above)
	 * during {@code TouhouMod#onInitialize}.
	 */
	public static void register() {
	}
}
