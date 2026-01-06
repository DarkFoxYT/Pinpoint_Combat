package net.goat.client.badge;

import net.minecraft.util.Identifier;

public record BadgeStyle(Identifier fontId, String glyph, int color) {
}
