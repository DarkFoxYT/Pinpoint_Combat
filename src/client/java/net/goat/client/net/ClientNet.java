package net.goat.client.net;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.goat.badge.BadgeState;
import net.goat.badge.InvBadgePicker;
import net.goat.net.Packets;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;

public final class ClientNet {
    private static Identifier lastSent;

    private ClientNet() {}

    public static void initClient() {
        // Receive server sync
        ClientPlayNetworking.registerGlobalReceiver(Packets.S2C_BADGE_SYNC_ID, (payload, context) -> {
            UUID uuid = payload.playerUuid();
            Identifier badge = payload.badgeItemId().orElse(null);

            context.client().execute(() -> BadgeState.set(uuid, badge));
        });

        // Detect best badge and notify server
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.getNetworkHandler() == null) return;

            Identifier best = InvBadgePicker.pickBestBadgeItemId(client.player);
            if (same(best, lastSent)) return;

            lastSent = best;
            ClientPlayNetworking.send(new Packets.C2SSetBadgePayload(Optional.ofNullable(best)));
        });
    }

    private static boolean same(Identifier a, Identifier b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
