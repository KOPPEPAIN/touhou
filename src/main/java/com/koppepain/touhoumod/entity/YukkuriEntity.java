package com.koppepain.touhoumod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import com.koppepain.touhoumod.entity.ai.YukkuriDanmakuAttackGoal;

/**
 * "Yukkuri" - a small, round, flattened creature (an original-art homage to
 * the long-running Japanese "yukkuri" internet fan meme) that wanders
 * peacefully until provoked, at which point it unleashes danmaku bullet
 * patterns at its target like a miniature Touhou stage boss.
 */
public class YukkuriEntity extends HostileEntity {
	private static final TrackedData<Integer> DANMAKU_COLOR =
			DataTracker.registerData(YukkuriEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private static final int[] PALETTE = {
			0xFF6699, // pink (Reimu-ish)
			0xFFD24D, // yellow (Marisa-ish)
			0x66CCFF, // cyan (Cirno-ish)
			0xCC99FF  // purple
	};

	public YukkuriEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 8;
		this.dataTracker.set(DANMAKU_COLOR, PALETTE[this.random.nextInt(PALETTE.length)]);
	}

	public static DefaultAttributeContainer.Builder createYukkuriAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.MAX_HEALTH, 20.0)
				.add(EntityAttributes.MOVEMENT_SPEED, 0.2)
				.add(EntityAttributes.FOLLOW_RANGE, 24.0)
				.add(EntityAttributes.ATTACK_DAMAGE, 2.0);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new YukkuriDanmakuAttackGoal(this));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 10.0f));
		this.goalSelector.add(8, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(DANMAKU_COLOR, PALETTE[0]);
	}

	public int getDanmakuColor() {
		return this.dataTracker.get(DANMAKU_COLOR);
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("DanmakuColor", this.getDanmakuColor());
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("DanmakuColor")) {
			this.dataTracker.set(DANMAKU_COLOR, nbt.getInt("DanmakuColor"));
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SLIME_SQUISH_SMALL;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SLIME_HURT_SMALL;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SLIME_DEATH_SMALL;
	}
}
