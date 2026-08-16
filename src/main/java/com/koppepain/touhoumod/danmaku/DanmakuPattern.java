package com.koppepain.touhoumod.danmaku;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import com.koppepain.touhoumod.entity.DanmakuBulletEntity;

/**
 * Bullet-pattern ("danmaku") spawning helpers, loosely modelled after the
 * classic Touhou Danmakufu scripting idioms (ring spreads defined by a shot
 * count / radius / speed, aimed spreads, and rotating spiral arms).
 *
 * All angles here are plain XZ-plane trigonometry (not Minecraft yaw), since
 * bullets are billboard-rendered and don't need to track an entity facing.
 */
public final class DanmakuPattern {
	private DanmakuPattern() {
	}

	/** An even ring of {@code count} bullets, offset by a random starting angle each call. */
	public static void ring(ServerWorld world, LivingEntity owner, double x, double y, double z,
			int count, float speed, int color) {
		double baseAngle = world.getRandom().nextDouble() * 360.0;
		for (int i = 0; i < count; i++) {
			double angle = baseAngle + (360.0 / count) * i;
			spawnAtAngle(world, owner, x, y, z, angle, speed, color);
		}
	}

	/** A narrow spread of {@code count} bullets aimed at {@code targetPos}. */
	public static void aimedSpread(ServerWorld world, LivingEntity owner, Vec3d targetPos,
			double x, double y, double z, int count, float speed, float spreadDegrees, int color) {
		Vec3d dir = targetPos.subtract(x, y, z);
		if (dir.lengthSquared() < 1.0E-4) {
			dir = new Vec3d(0, 0, 1);
		}
		double baseAngle = Math.toDegrees(Math.atan2(dir.z, dir.x));
		double start = baseAngle - spreadDegrees / 2.0;
		double step = count > 1 ? spreadDegrees / (count - 1) : 0;
		for (int i = 0; i < count; i++) {
			spawnAtAngle(world, owner, x, y, z, start + step * i, speed, color);
		}
	}

	/**
	 * One "frame" of a spiral pattern: {@code armCount} bullets spaced evenly
	 * around a ring that is rotated a little further each call. Call this
	 * once per game tick (or every few ticks) from a goal/AI loop, passing an
	 * ever-increasing {@code tickCounter}, to build up a spiral over time.
	 */
	public static void spiralStep(ServerWorld world, LivingEntity owner, double x, double y, double z,
			int armCount, float speed, int color, int tickCounter, double degreesPerStep) {
		double rotation = tickCounter * degreesPerStep;
		for (int i = 0; i < armCount; i++) {
			double angle = rotation + (360.0 / armCount) * i;
			spawnAtAngle(world, owner, x, y, z, angle, speed, color);
		}
	}

	private static void spawnAtAngle(ServerWorld world, LivingEntity owner, double x, double y, double z,
			double angleDegrees, float speed, int color) {
		double rad = Math.toRadians(angleDegrees);
		Vec3d velocity = new Vec3d(Math.cos(rad), 0.0, Math.sin(rad)).multiply(speed);
		spawn(world, owner, x, y, z, velocity, color);
	}

	private static void spawn(ServerWorld world, LivingEntity owner, double x, double y, double z,
			Vec3d velocity, int color) {
		DanmakuBulletEntity bullet = new DanmakuBulletEntity(world, x, y, z, velocity, color);
		bullet.setOwner(owner);
		world.spawnEntity(bullet);
	}
}
