package net.goat.badge;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Predicate;

public record BadgeRule(Identifier badgeId, Predicate<ServerPlayerEntity> condition) {
    public BadgeRule {
        Objects.requireNonNull(badgeId, "badgeId");
        Objects.requireNonNull(condition, "condition");
    }
}
