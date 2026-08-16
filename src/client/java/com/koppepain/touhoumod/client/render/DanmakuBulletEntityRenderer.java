package com.koppepain.touhoumod.client.render;

import org.joml.Matrix4f;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.DanmakuBulletEntity;

/**
 * Renders each danmaku bullet as a small camera-facing glowing quad, drawn
 * through the custom additive glow shader in {@link ModRenderLayers} /
 * {@link TouhouShaders} rather than a normal lit entity model.
 */
public class DanmakuBulletEntityRenderer extends EntityRenderer<DanmakuBulletEntity> {
	private static final Identifier TEXTURE = Identifier.of(TouhouMod.MOD_ID, "textures/entity/danmaku_bullet.png");
	private static final float SIZE = 0.28f;
	private static final int FULL_BRIGHT = LightmapTextureManager.pack(15, 15);

	public DanmakuBulletEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(DanmakuBulletEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(DanmakuBulletEntity entity, float yaw, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light) {
		matrices.push();

		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(SIZE, SIZE, SIZE);

		VertexConsumer buffer = vertexConsumers.getBuffer(ModRenderLayers.getDanmakuBullet(TEXTURE));
		Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

		int color = entity.getColor();
		float r = ((color >> 16) & 0xFF) / 255.0f;
		float g = ((color >> 8) & 0xFF) / 255.0f;
		float b = (color & 0xFF) / 255.0f;

		float pulse = 1.0f + 0.15f * MathHelper.sin((entity.age + tickDelta) * 0.6f);

		vertex(buffer, positionMatrix, -pulse, -pulse, r, g, b, 0.0f, 0.0f);
		vertex(buffer, positionMatrix, -pulse, pulse, r, g, b, 0.0f, 1.0f);
		vertex(buffer, positionMatrix, pulse, pulse, r, g, b, 1.0f, 1.0f);
		vertex(buffer, positionMatrix, pulse, -pulse, r, g, b, 1.0f, 0.0f);

		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}

	private static void vertex(VertexConsumer buffer, Matrix4f matrix, float x, float y,
			float r, float g, float b, float u, float v) {
		buffer.vertex(matrix, x, y, 0.0f)
				.color(r, g, b, 1.0f)
				.texture(u, v)
				.light(FULL_BRIGHT);
	}
}
