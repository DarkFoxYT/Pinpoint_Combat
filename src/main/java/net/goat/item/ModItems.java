package net.goat.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.goat.badge.BadgeStyle;
import net.goat.badge.BadgeStyleRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {
    private ModItems() {}

    public static final Item BADGE_RED = new Item(new Item.Settings());
    public static final Item BADGE_BLUE = new Item(new Item.Settings());
    public static final Item BADGE_GREEN = new Item(new Item.Settings());

    public static void init() {
        Registry.register(Registries.ITEM, Identifier.of("pinpoint_combat", "badge_red"), BADGE_RED);
        Registry.register(Registries.ITEM, Identifier.of("pinpoint_combat", "badge_blue"), BADGE_BLUE);
        Registry.register(Registries.ITEM, Identifier.of("pinpoint_combat", "badge_green"), BADGE_GREEN);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(BADGE_RED);
            entries.add(BADGE_BLUE);
            entries.add(BADGE_GREEN);
        });

        // Your font file already exists: assets/pinpoint_combat/font/badges.json
        Identifier badgeFont = Identifier.of("pinpoint_combat", "badges");

        // Choose glyphs that exist in your font json.
        // If your font uses different glyphs, change these strings.
        BadgeStyleRegistry.register(BADGE_RED,   new BadgeStyle(0xFF3333, "\uE001", badgeFont));
        BadgeStyleRegistry.register(BADGE_BLUE,  new BadgeStyle(0x3399FF, "\uE002", badgeFont));
        BadgeStyleRegistry.register(BADGE_GREEN, new BadgeStyle(0x33FF66, "\uE003", badgeFont));
    }
}
