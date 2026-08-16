package com.koppepain.touhoumod.entity.ai;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;

import com.koppepain.touhoumod.danmaku.DanmakuPattern;
import com.koppepain.touhoumod.entity.YukkuriEntity;

/**
 * Makes a {@link YukkuriEntity} periodically fire a ring danmaku pattern at
 * its current attack target, alternating with slow rotating "spiral" bursts
 * once it has been fighting for a while - a small nod to Touhou boss
 * non-spell/spell-card pacing.
 */
public class YukkuriDanmakuAttackGoal extends Goal {
	private static final int RING_INTERVAL = 50;
	private static final int SPIRAL_SWITCH_TICKS = 200;

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

		if (this.fightTicks < SPIRAL_SWITCH_TICKS) {
			DanmakuPattern.ring(serverWorld, this.yukkuri, x, y, z, 16, 0.35f, color);
			this.cooldown = RING_INTERVAL;
		} else {
			DanmakuPattern.spiralStep(serverWorld, this.yukkuri, x, y, z, 3, 0.3f, color, this.fightTicks, 12.0);
			this.cooldown = 4;
		}
	}
}
