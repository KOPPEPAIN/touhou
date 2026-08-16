package com.koppepain.touhoumod.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.DanmakuBulletEntity;
import com.koppepain.touhoumod.entity.YukkuriEntity;

/**
 * Entity type registration.
 *
 * NOTE: EntityType.Builder#build requires the target RegistryKey on 1.20.5+
 * mappings (Mojang added mandatory registry keys to most builders). If your
 * Yarn build still exposes the old String-based #build(String) overload,
 * adjust the calls below accordingly.
 */
public final class ModEntities {
	private static final RegistryKey<EntityType<YukkuriEntity>> YUKKURI_KEY =
			RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TouhouMod.MOD_ID, "yukkuri"));
	public static final EntityType<YukkuriEntity> YUKKURI = Registry.register(
			Registries.ENTITY_TYPE,
			YUKKURI_KEY,
			EntityType.Builder.create(YukkuriEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.9f, 0.6f)
					.maxTrackingRange(48)
					.trackingTickInterval(3)
					.build(YUKKURI_KEY));

	private static final RegistryKey<EntityType<DanmakuBulletEntity>> DANMAKU_BULLET_KEY =
			RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TouhouMod.MOD_ID, "danmaku_bullet"));
	public static final EntityType<DanmakuBulletEntity> DANMAKU_BULLET = Registry.register(
			Registries.ENTITY_TYPE,
			DANMAKU_BULLET_KEY,
			EntityType.Builder.<DanmakuBulletEntity>create(DanmakuBulletEntity::new, SpawnGroup.MISC)
					.dimensions(0.25f, 0.25f)
					.maxTrackingRange(64)
					.trackingTickInterval(1)
					.build(DANMAKU_BULLET_KEY));

	private ModEntities() {
	}

	/**
	 * Forces this class to load (and thus run the static registrations above)
	 * during {@code TouhouMod#onInitialize}.
	 */
	public static void register() {
	}
}
