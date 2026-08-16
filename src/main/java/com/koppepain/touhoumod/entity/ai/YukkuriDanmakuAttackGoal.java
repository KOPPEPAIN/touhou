package com.koppepain.touhoumod.entity.ai;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import com.koppepain.touhoumod.danmaku.DanmakuPattern;
import com.koppepain.touhoumod.entity.YukkuriEntity;

/**
 * Makes a {@link YukkuriEntity} cycle through three increasingly dense
 * danmaku "phases" while fighting - a small nod to Touhou boss spell-card
 * pacing:
 * <ol>
 *   <li>a double ring burst (two overlapping rings of bullets)</li>
 *   <li>a fast rotating spiral</li>
 *   <li>aimed spread volleys at the target</li>
 * </ol>
 * ...then loops back to the start for as long as the fight continues.
 */
public class YukkuriDanmakuAttackGoal extends Goal {
	private static final int CYCLE_LENGTH = 320;
	private static final int RING_PHASE_END = 150;
	private static final int SPIRAL_PHASE_END = 260;

	private static final int RING_BULLET_COUNT = 24;
	private static final int RING_INTERVAL = 25;

	private static final int SPIRAL_ARMS = 6;
	private static final int SPIRAL_INTERVAL = 3;

	private static final int AIMED_BULLET_COUNT = 14;
	private static final int AIMED_INTERVAL = 18;

	private final YukkuriEntity yukkuri;
	private int cooldown;
	private int fightTicks;

	public YukkuriDanmakuAttackGoal(YukkuriEntity yukkuri) {
		this.yukkuri = yukkuri;
		this.setControls(EnumSet.of(Control.LOOK));
	}

	@Override
	public boolean canStart() {
		LivingEntity target = this.yukkuri.getTarget();
		return target != null && target.isAlive();
	}

	@Override
	public boolean shouldContinue() {
		LivingEntity target = this.yukkuri.getTarget();
		return target != null && target.isAlive() && this.yukkuri.canSee(target);
	}

	@Override
	public void start() {
		this.cooldown = 20;
		this.fightTicks = 0;
	}

	@Override
	public void stop() {
		this.fightTicks = 0;
	}

	@Override
	public void tick() {
		LivingEntity target = this.yukkuri.getTarget();
		if (target == null) {
			return;
		}

		this.yukkuri.getLookControl().lookAt(target, 30.0f, 30.0f);
		this.fightTicks++;
		this.cooldown--;

		if (this.cooldown > 0 || !(this.yukkuri.getWorld() instanceof ServerWorld serverWorld)) {
			return;
		}

		double x = this.yukkuri.getX();
		double y = this.yukkuri.getY() + this.yukkuri.getHeight() * 0.5;
		double z = this.yukkuri.getZ();
		int color = this.yukkuri.getDanmakuColor();
		int phase = this.fightTicks % CYCLE_LENGTH;

		if (phase < RING_PHASE_END) {
			DanmakuPattern.ring(serverWorld, this.yukkuri, x, y, z, RING_BULLET_COUNT, 0.35f, color);
			DanmakuPattern.ring(serverWorld, this.yukkuri, x, y, z, RING_BULLET_COUNT, 0.22f, color);
			this.cooldown = RING_INTERVAL;
		} else if (phase < SPIRAL_PHASE_END) {
			DanmakuPattern.spiralStep(serverWorld, this.yukkuri, x, y, z, SPIRAL_ARMS, 0.3f, color, this.fightTicks, 10.0);
			this.cooldown = SPIRAL_INTERVAL;
		} else {
			Vec3d targetPos = new Vec3d(target.getX(), target.getEyeY(), target.getZ());
			DanmakuPattern.aimedSpread(serverWorld, this.yukkuri, targetPos, x, y, z, AIMED_BULLET_COUNT, 0.45f, 60.0f, color);
			this.cooldown = AIMED_INTERVAL;
		}
	}
}
