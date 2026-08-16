package com.koppepain.touhoumod.client.render;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.YukkuriEntity;

/**
 * A simple flattened "pancake" box model - matching the round, squashed
 * silhouette of the yukkuri fan-art style - with a gentle idle bob. The face
 * itself lives entirely in the texture (see {@code textures/entity/yukkuri.png}).
 */
public class YukkuriEntityModel extends SinglePartEntityModel<YukkuriEntity> {
	public static final EntityModelLayer LAYER =
			new EntityModelLayer(Identifier.of(TouhouMod.MOD_ID, "yukkuri"), "main");

	private final ModelPart root;
	private final ModelPart body;

	public YukkuriEntityModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData rootData = modelData.getRoot();
		rootData.addChild("body",
				ModelPartBuilder.create()
						.uv(0, 0)
						.cuboid(-6.0f, -4.0f, -6.0f, 12.0f, 4.0f, 12.0f),
				ModelTransform.pivot(0.0f, 20.0f, 0.0f));
		return TexturedModelData.of(modelData, 64, 32);
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

	@Override
	public void setAngles(YukkuriEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.body.pivotY = 20.0f + MathHelper.sin(animationProgress * 0.2f) * 0.6f;
		this.body.yaw = headYaw * ((float) Math.PI / 180F);
	}
}
