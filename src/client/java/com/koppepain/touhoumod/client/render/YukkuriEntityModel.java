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
 * A rounded, domed "yukkuri" body - three stacked boxes shrinking toward the
 * top approximate a squashed hemisphere, with two tiny stub arms poking out
 * the sides (the iconic tiny-limbed yukkuri silhouette). The character's
 * face/hair/accessory colouring lives entirely in the texture (see
 * {@code textures/entity/yukkuri/*.png}), painted onto the topmost box like
 * the flat painted-on faces of the original fan-art style.
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
		ModelPartData root = modelData.getRoot();

		ModelPartData body = root.addChild("body",
				ModelPartBuilder.create()
						.uv(0, 0).cuboid(-7.0f, -2.0f, -7.0f, 14.0f, 2.0f, 14.0f)
						.uv(0, 24).cuboid(-6.0f, -5.0f, -6.0f, 12.0f, 3.0f, 12.0f)
						.uv(0, 44).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 3.0f, 8.0f),
				ModelTransform.pivot(0.0f, 22.0f, 0.0f));

		body.addChild("left_arm",
				ModelPartBuilder.create().uv(48, 0).cuboid(-2.0f, -1.0f, -2.0f, 2.0f, 2.0f, 4.0f),
				ModelTransform.pivot(-8.0f, -4.0f, 0.0f));
		body.addChild("right_arm",
				ModelPartBuilder.create().uv(48, 8).cuboid(0.0f, -1.0f, -2.0f, 2.0f, 2.0f, 4.0f),
				ModelTransform.pivot(8.0f, -4.0f, 0.0f));

		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public ModelPart getPart() {
		return this.root;
	}

	@Override
	public void setAngles(YukkuriEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.body.pivotY = 22.0f + MathHelper.sin(animationProgress * 0.2f) * 0.6f;
		this.body.yaw = headYaw * ((float) Math.PI / 180F);
	}
}
