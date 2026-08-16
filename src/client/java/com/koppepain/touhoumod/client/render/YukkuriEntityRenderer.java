package com.koppepain.touhoumod.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.YukkuriEntity;

public class YukkuriEntityRenderer extends MobEntityRenderer<YukkuriEntity, YukkuriEntityModel> {
	private static final Identifier TEXTURE = Identifier.of(TouhouMod.MOD_ID, "textures/entity/yukkuri.png");

	public YukkuriEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new YukkuriEntityModel(context.getPart(YukkuriEntityModel.LAYER)), 0.35f);
	}

	@Override
	public Identifier getTexture(YukkuriEntity entity) {
		return TEXTURE;
	}
}
