package net.goat.badge;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public final class BadgeStyleRegistry {
    private static final Map<Identifier, BadgeStyle> BY_ITEM_ID = new HashMap<>();

    private BadgeStyleRegistry() {}

    public static void register(Item item, BadgeStyle style) {
        Identifier id = Registries.ITEM.getId(item);
        BY_ITEM_ID.put(id, style);
    }

    public static BadgeStyle getByItemId(Identifier itemId) {
        return BY_ITEM_ID.get(itemId);
    }

    public static boolean isBadgeItem(Identifier itemId) {
        return BY_ITEM_ID.containsKey(itemId);
    }
}
