package com.koppepain.touhoumod.client.render;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;

import com.koppepain.touhoumod.TouhouMod;

/**
 * Registers the custom "glow" core shader used to render danmaku bullets
 * (see {@code assets/touhoumod/shaders/core/danmaku_bullet.{json,vsh,fsh}}).
 *
 * NOTE ON MAPPINGS: Mojang renamed the old {@code ShaderInstance}/{@code Shader}
 * class to {@code ShaderProgram} around 1.20.5-1.21. If your Yarn build still
 * uses the old name, or if {@link net.minecraft.client.render.RenderPhase}'s
 * nested shader-phase class in {@link ModRenderLayers} is called
 * {@code RenderPhase.Shader} instead of {@code RenderPhase.ShaderProgram},
 * update both files' type references accordingly - this is the single
 * riskiest spot in the mod for a mappings mismatch.
 */
public final class TouhouShaders {
	private static final Identifier DANMAKU_BULLET_ID = Identifier.of(TouhouMod.MOD_ID, "danmaku_bullet");

	private static ShaderProgram danmakuBulletShader;

	private TouhouShaders() {
	}

	public static void register() {
		CoreShaderRegistrationCallback.EVENT.register(context -> context.register(
				DANMAKU_BULLET_ID,
				VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
				shader -> danmakuBulletShader = shader));
	}

	public static ShaderProgram getDanmakuBulletShader() {
		return danmakuBulletShader;
	}
}
