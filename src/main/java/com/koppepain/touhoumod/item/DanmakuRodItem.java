package com.koppepain.touhoumod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.koppepain.touhoumod.danmaku.DanmakuPattern;

/**
 * A player-usable "danmaku rod": right-click to unleash a ring of danmaku
 * bullets outward from the player, in the same style as {@link
 * com.koppepain.touhoumod.entity.YukkuriEntity}'s attack.
 */
public class DanmakuRodItem extends Item {
	private static final int COOLDOWN_TICKS = 15;
	private static final int BULLET_COUNT = 20;
	private static final float BULLET_SPEED = 0.55f;
	private static final int BULLET_COLOR = 0x66E0FF;

	public DanmakuRodItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user.getItemCooldownManager().isCoolingDown(this)) {
			return TypedActionResult.fail(stack);
		}

		if (world instanceof ServerWorld serverWorld) {
			double x = user.getX();
			double y = user.getEyeY() - 0.2;
			double z = user.getZ();
			Vec3d look = user.getRotationVec(1.0f);
			Vec3d target = new Vec3d(x, y, z).add(look.multiply(4.0));

			DanmakuPattern.aimedSpread(serverWorld, user, target, x, y, z, BULLET_COUNT, BULLET_SPEED, 80.0f, BULLET_COLOR);
			DanmakuPattern.aimedSpread(serverWorld, user, target, x, y, z, BULLET_COUNT / 2, BULLET_SPEED * 1.3f, 40.0f, BULLET_COLOR);

			world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL,
					SoundCategory.PLAYERS, 0.8f, 1.4f);
			user.getItemCooldownManager().set(this, COOLDOWN_TICKS);
		}

		user.swingHand(hand, true);
		return TypedActionResult.success(stack);
	}
}
