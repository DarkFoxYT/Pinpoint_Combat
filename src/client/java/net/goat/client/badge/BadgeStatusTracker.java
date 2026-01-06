package net.goat.client.badge;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class BadgeStatusTracker {
    private static final Map<Identifier, BadgeStyle> STYLES = new HashMap<>();
    private static final Map<UUID, Identifier> BADGES_BY_PLAYER = new HashMap<>();

    private BadgeStatusTracker() {
    }

    public static void registerStyle(Identifier badgeId, BadgeStyle style) {
        Objects.requireNonNull(badgeId, "badgeId");
        Objects.requireNonNull(style, "style");
        STYLES.put(badgeId, style);
    }

    public static BadgeStyle getStyle(UUID playerId) {
        Identifier badgeId = BADGES_BY_PLAYER.get(playerId);
        return badgeId != null ? STYLES.get(badgeId) : null;
    }

    public static void update(Map<UUID, Identifier> updates) {
        BADGES_BY_PLAYER.clear();
        BADGES_BY_PLAYER.putAll(updates);
    }

    public static Map<UUID, Identifier> snapshot() {
        return Collections.unmodifiableMap(BADGES_BY_PLAYER);
    }
}
