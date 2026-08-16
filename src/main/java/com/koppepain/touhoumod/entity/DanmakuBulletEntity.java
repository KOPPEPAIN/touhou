package com.koppepain.touhoumod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.koppepain.touhoumod.registry.ModEntities;

/**
 * A single "danmaku" bullet: a small, non-gravity, constant-velocity
 * projectile fired in patterns by {@link com.koppepain.touhoumod.danmaku.DanmakuPattern}.
 *
 * Rendering-wise this is a billboard quad drawn through a custom glow core
 * shader (see the client-side {@code DanmakuBulletEntityRenderer} /
 * {@code ModRenderLayers} / {@code TouhouShaders}), not a normal entity
 * model - the tracked {@link #COLOR} data lets each pattern tint its bullets.
 */
public class DanmakuBulletEntity extends ProjectileEntity {
	private static final TrackedData<Integer> COLOR =
			DataTracker.registerData(DanmakuBulletEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private static final int DEFAULT_MAX_AGE = 160;

	private float damage = 2.0f;
	private int maxAge = DEFAULT_MAX_AGE;

	public DanmakuBulletEntity(EntityType<? extends DanmakuBulletEntity> type, World world) {
		super(type, world);
		this.setNoGravity(true);
	}

	public DanmakuBulletEntity(World world, double x, double y, double z, Vec3d velocity, int color) {
		this(ModEntities.DANMAKU_BULLET, world);
		this.setPosition(x, y, z);
		this.setVelocity(velocity);
		this.dataTracker.set(COLOR, color);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		builder.add(COLOR, 0xFF66CC);
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}

	public DanmakuBulletEntity setDamage(float damage) {
		this.damage = damage;
		return this;
	}

	public DanmakuBulletEntity setMaxAge(int maxAge) {
		this.maxAge = maxAge;
		return this;
	}

	@Override
	public void tick() {
		super.tick();

		HitResult hit = ProjectileUtil.getCollision(this, this::canHitEntity);
		if (hit.getType() != HitResult.Type.MISS) {
			this.onCollision(hit);
		}

		if (!this.isRemoved()) {
			Vec3d velocity = this.getVelocity();
			Vec3d next = this.getPos().add(velocity);
			this.setPosition(next.x, next.y, next.z);
			this.age++;
			if (this.age > this.maxAge) {
				this.discard();
			}
		}
	}

	private boolean canHitEntity(Entity entity) {
		if (!entity.isAlive() || entity.isSpectator() || !entity.canHit()) {
			return false;
		}
		Entity owner = this.getOwner();
		return owner == null || this.age >= 3 || !entity.equals(owner);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (this.getWorld().isClient) {
			return;
		}
		Entity target = entityHitResult.getEntity();
		if (target instanceof LivingEntity living && this.getWorld() instanceof ServerWorld serverWorld) {
			Entity owner = this.getOwner();
			var source = owner instanceof LivingEntity livingOwner
					? serverWorld.getDamageSources().mobProjectile(this, livingOwner)
					: serverWorld.getDamageSources().magic();
			living.damage(serverWorld, source, this.damage);
		}
		this.discard();
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		if (!this.getWorld().isClient) {
			this.discard();
		}
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.contains("Color")) {
			this.dataTracker.set(COLOR, nbt.getInt("Color"));
		}
		if (nbt.contains("Damage")) {
			this.damage = nbt.getFloat("Damage");
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt("Color", this.getColor());
		nbt.putFloat("Damage", this.damage);
	}
}
