package net.goat.badge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class InvBadgePicker {
    private InvBadgePicker() {}

    public static Identifier pickBestBadgeItemId(PlayerEntity player) {
        // Simple: first badge found in inventory wins.
        // "Advanced" idea: add priority numbers to BadgeStyle and pick highest.
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;

            Identifier id = Registries.ITEM.getId(stack.getItem());
            if (BadgeStyleRegistry.isBadgeItem(id)) return id;
        }
        return null;
    }
}
