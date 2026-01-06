package net.goat;

import net.fabricmc.api.ModInitializer;
import net.goat.network.BadgeStateSynchronizer;
import net.minecraft.util.Identifier;

public class PinpointCombat implements ModInitializer {
    public static final String MOD_ID = "pinpoint_combat";
    public static final Identifier BADGE_STATUS_PACKET = Identifier.of(MOD_ID, "badge_status");
    public static final Identifier BELL_BADGE_ID = Identifier.of(MOD_ID, "bell_badge");

    @Override
    public void onInitialize() {
        BadgeStateSynchronizer.registerDefaults();
        BadgeStateSynchronizer.registerEvents();
    }
}
