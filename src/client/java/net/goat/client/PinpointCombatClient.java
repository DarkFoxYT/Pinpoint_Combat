package net.goat.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.goat.PinpointCombat;
import net.goat.client.badge.BadgeStatusTracker;
import net.goat.client.badge.BadgeStyle;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PinpointCombatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BadgeStatusTracker.registerStyle(PinpointCombat.BELL_BADGE_ID, new BadgeStyle(Style.DEFAULT_FONT_ID, "\uD83D\uDD14", 0xF7D64C));

        ClientPlayNetworking.registerGlobalReceiver(PinpointCombat.BADGE_STATUS_PACKET, (client, handler, buf, responseSender) -> {
            int count = buf.readVarInt();
            Map<UUID, Identifier> badgeAssignments = new HashMap<>();
            for (int i = 0; i < count; i++) {
                UUID playerId = buf.readUuid();
                Identifier badgeId = buf.readIdentifier();
                badgeAssignments.put(playerId, badgeId);
            }

            client.execute(() -> BadgeStatusTracker.update(badgeAssignments));
        });
    }
}
