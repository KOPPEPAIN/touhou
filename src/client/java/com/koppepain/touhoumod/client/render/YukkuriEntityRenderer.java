package com.koppepain.touhoumod.client.render;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.entity.YukkuriEntity;
import com.koppepain.touhoumod.entity.YukkuriEntity.Variant;

public class YukkuriEntityRenderer extends MobEntityRenderer<YukkuriEntity, YukkuriEntityModel> {
	private static final Map<Variant, Identifier> TEXTURES = new EnumMap<>(Variant.class);

	static {
		for (Variant variant : Variant.values()) {
			TEXTURES.put(variant, Identifier.of(TouhouMod.MOD_ID, "textures/entity/yukkuri/" + variant.id + ".png"));
		}
	}

	public YukkuriEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new YukkuriEntityModel(context.getPart(YukkuriEntityModel.LAYER)), 0.4f);
	}

	@Override
	public Identifier getTexture(YukkuriEntity entity) {
		return TEXTURES.get(entity.getVariant());
	}
}
