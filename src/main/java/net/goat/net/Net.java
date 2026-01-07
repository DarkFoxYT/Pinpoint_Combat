package net.goat.net;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.goat.badge.BadgeState;
import net.goat.badge.InvBadgePicker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;

public final class Net {
    private Net() {}

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(Packets.C2S_SET_BADGE_ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            context.server().execute(() -> {
                Optional<Identifier> badge = payload.badgeItemId();
                BadgeState.set(player.getUuid(), badge.orElse(null));
                syncBadgeToTracking(player, badge.orElse(null));
            });
        });
    }

    public static void serverTick(ServerPlayerEntity player) {
        Identifier best = InvBadgePicker.pickBestBadgeItemId(player);
        Identifier current = BadgeState.get(player.getUuid());
        if ((best == null && current != null) || (best != null && !best.equals(current))) {
            BadgeState.set(player.getUuid(), best);
            syncBadgeToTracking(player, best);
        }
    }

    public static void syncBadgeToTracking(ServerPlayerEntity owner, Identifier badgeItemIdOrNull) {
        UUID uuid = owner.getUuid();
        Optional<Identifier> opt = Optional.ofNullable(badgeItemIdOrNull);

        ServerPlayNetworking.send(owner, new Packets.S2CBadgeSyncPayload(uuid, opt));
        for (ServerPlayerEntity watcher : PlayerLookup.tracking(owner)) {
            ServerPlayNetworking.send(watcher, new Packets.S2CBadgeSyncPayload(uuid, opt));
        }
    }
}
