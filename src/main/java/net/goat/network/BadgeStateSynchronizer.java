package net.goat.network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.goat.PinpointCombat;
import net.goat.badge.BadgeRule;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public final class BadgeStateSynchronizer {
    private static final int SYNC_INTERVAL_TICKS = 20;
    private static final List<BadgeRule> RULES = new ArrayList<>();
    private static int tickCountdown = SYNC_INTERVAL_TICKS;

    private BadgeStateSynchronizer() {
    }

    public static void registerDefaults() {
        register(BadgeStateSynchronizer.createInventoryRule(PinpointCombat.BELL_BADGE_ID, stack -> !stack.isEmpty() && stack.isOf(Items.PUMPKIN_SEEDS)));
    }

    public static void register(BadgeRule rule) {
        RULES.add(rule);
    }

    public static void registerInventoryBadge(Identifier badgeId, Predicate<ItemStack> condition) {
        register(createInventoryRule(badgeId, condition));
    }

    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(BadgeStateSynchronizer::handleTick);
    }

    private static void handleTick(MinecraftServer server) {
        tickCountdown--;
        if (tickCountdown > 0) {
            return;
        }

        tickCountdown = SYNC_INTERVAL_TICKS;
        syncBadges(server);
    }

    private static void syncBadges(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        Map<UUID, Identifier> activeBadges = new HashMap<>();

        for (ServerPlayerEntity player : players) {
            Identifier badge = resolveBadge(player);
            if (badge != null) {
                activeBadges.put(player.getUuid(), badge);
            }
        }

        for (ServerPlayerEntity receiver : players) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(activeBadges.size());
            activeBadges.forEach((uuid, badgeId) -> {
                buf.writeUuid(uuid);
                buf.writeIdentifier(badgeId);
            });

            ServerPlayNetworking.send(receiver, PinpointCombat.BADGE_STATUS_PACKET, buf);
        }
    }

    private static Identifier resolveBadge(ServerPlayerEntity player) {
        for (BadgeRule rule : RULES) {
            if (rule.condition().test(player)) {
                return rule.badgeId();
            }
        }
        return null;
    }

    private static BadgeRule createInventoryRule(Identifier badgeId, Predicate<ItemStack> condition) {
        return new BadgeRule(badgeId, player -> {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (condition.test(stack)) {
                    return true;
                }
            }
            return false;
        });
    }
}
