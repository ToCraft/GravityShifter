package dev.tocraft.gravityshifter.fabric;

import dev.tocraft.gravityshifter.GravityShifter;
import dev.tocraft.gravityshifter.api.GravityData;
import dev.tocraft.gravityshifter.network.ServerNetwork;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

public class GravityShifterFabric {
    public static void initialize() {
        GravityShifter.initialize();

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (trackedEntity instanceof LivingEntity living) {
                Direction direction = GravityData.getGravity(living);
                ServerNetwork.syncEntityGravity(living, player.level(), direction);
            }
        });
    }
}
