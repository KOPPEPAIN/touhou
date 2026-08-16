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
 * "Yukkuri" - a small, round creature (an original-art homage to the
 * long-running Japanese "yukkuri" internet fan meme, itself a simplified,
 * squashed-face take on Touhou Project characters) that wanders peacefully
 * until provoked, at which point it unleashes danmaku bullet patterns at its
 * target like a miniature Touhou stage boss.
 *
 * Each yukkuri spawns as one of a few stylised, simplified character
 * "flavours" ({@link Variant}) that pick both its texture and the colour of
 * the danmaku it fires.
 */
public class YukkuriEntity extends HostileEntity {
	/** Simplified, stylised character flavour - not a 1:1 likeness of any official artwork. */
	public enum Variant {
		REIMU("reimu", 0xFF3B3B),
		MARISA("marisa", 0xFFC64D),
		CIRNO("cirno", 0x4DD2FF),
		GENERIC("generic", 0xCC99FF);

		public final String id;
		public final int danmakuColor;

		Variant(String id, int danmakuColor) {
			this.id = id;
			this.danmakuColor = danmakuColor;
		}

		public static Variant byIndex(int index) {
			Variant[] values = values();
			return values[Math.floorMod(index, values.length)];
		}
	}

	private static final TrackedData<Integer> VARIANT =
			DataTracker.registerData(YukkuriEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public YukkuriEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 8;
		this.dataTracker.set(VARIANT, this.random.nextInt(Variant.values().length));
	}

	public static DefaultAttributeContainer.Builder createYukkuriAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24.0)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
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
		builder.add(VARIANT, Variant.GENERIC.ordinal());
	}

	public Variant getVariant() {
		return Variant.byIndex(this.dataTracker.get(VARIANT));
	}

	public int getDanmakuColor() {
		return this.getVariant().danmakuColor;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("YukkuriVariant", this.getVariant().ordinal());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("YukkuriVariant")) {
			this.dataTracker.set(VARIANT, nbt.getInt("YukkuriVariant"));
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
