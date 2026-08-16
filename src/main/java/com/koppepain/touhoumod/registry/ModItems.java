package com.koppepain.touhoumod.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import com.koppepain.touhoumod.TouhouMod;
import com.koppepain.touhoumod.item.DanmakuRodItem;

/**
 * Item registration.
 *
 * NOTE: Item.Settings#registryKey(RegistryKey) is required on 1.20.5+
 * mappings before the item can be registered. If your Yarn build doesn't
 * expose it, drop the .registryKey(...) call below.
 */
public final class ModItems {
	private static final RegistryKey<Item> DANMAKU_ROD_KEY =
			RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TouhouMod.MOD_ID, "danmaku_rod"));
	public static final Item DANMAKU_ROD = Registry.register(
			Registries.ITEM,
			DANMAKU_ROD_KEY,
			new DanmakuRodItem(new Item.Settings().maxCount(1).registryKey(DANMAKU_ROD_KEY)));

	private ModItems() {
	}

	public static void register() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(DANMAKU_ROD));
	}
}
