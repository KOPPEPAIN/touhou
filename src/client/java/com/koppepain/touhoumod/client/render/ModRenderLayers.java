package com.koppepain.touhoumod.client.render;

import java.util.function.Function;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

/**
 * Custom {@link RenderLayer} for danmaku bullets: additive-blended,
 * unculled, unlit-by-scene-light quads drawn through the glow core shader
 * registered in {@link TouhouShaders}.
 *
 * {@code RenderLayer#of(...)} is {@code protected static}, so - following the
 * common modding convention - this class extends {@link RenderLayer} purely
 * to gain access to it; it is never itself instantiated.
 */
public final class ModRenderLayers extends RenderLayer {
	private static final Function<Identifier, RenderLayer> DANMAKU_BULLET_LAYERS = Util.memoize(texture -> {
		RenderLayer.MultiPhaseParameters parameters = RenderLayer.MultiPhaseParameters.builder()
				.program(new RenderPhase.ShaderProgram(TouhouShaders::getDanmakuBulletShader))
				.texture(new RenderPhase.Texture(texture, false, false))
				.transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
				.cull(RenderPhase.DISABLE_CULLING)
				.lightmap(RenderPhase.ENABLE_LIGHTMAP)
				.writeMaskState(RenderPhase.COLOR_MASK)
				.build(false);

		return RenderLayer.of(
				"touhoumod_danmaku_bullet",
				VertexFormats.POSITION_COLOR_TEXTURE_LIGHT,
				VertexFormat.DrawMode.QUADS,
				256,
				false,
				true,
				parameters);
	});

	private ModRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode,
			int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer getDanmakuBullet(Identifier texture) {
		return DANMAKU_BULLET_LAYERS.apply(texture);
	}
}
