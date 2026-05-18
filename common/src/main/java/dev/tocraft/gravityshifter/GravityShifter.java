package dev.tocraft.gravityshifter;

import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.craftedcore.event.common.PlayerEvents;
import dev.tocraft.craftedcore.platform.PlatformData;
import dev.tocraft.gravityshifter.api.GravityData;
import dev.tocraft.gravityshifter.command.GravityShifterCommand;
import dev.tocraft.gravityshifter.network.ClientNetwork;
import dev.tocraft.gravityshifter.network.ServerNetwork;
import net.minecraft.resources.Identifier;

public class GravityShifter {
    public static final String MODID = "gravityshifter";

    public static void initialize() {
        ServerNetwork.initialize();
        CommandEvents.REGISTRATION.register(new GravityShifterCommand());
        PlayerEvents.PLAYER_JOIN.register(player -> ServerNetwork.sendEntityGravitySync(player, player, GravityData.getGravity(player)));

        if (PlatformData.getEnv().isClient()) {
            ClientNetwork.initialize();
        }
    }

    public static Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(MODID, id);
    }
}
