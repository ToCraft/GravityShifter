package dev.tocraft.gravityshifter.api;

import com.mojang.logging.LogUtils;
import dev.tocraft.craftedcore.platform.PlatformData;
import dev.tocraft.gravityshifter.data.ClientGravityStorage;
import dev.tocraft.gravityshifter.data.GravDataProvider;
import dev.tocraft.gravityshifter.data.GravityServerData;
import dev.tocraft.gravityshifter.network.ServerNetwork;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GravityData {
    public static void setGravity(LivingEntity entity, Direction direction) {
        ((GravDataProvider) entity).gravityShifter$setGravityDirection(direction);
        if (entity.level() instanceof ServerLevel serverLevel) {
            ServerNetwork.syncEntityGravity(entity, serverLevel, direction);
        }
    }

    @NotNull
    public static Direction getGravity(LivingEntity entity) {
        Direction direction = ((GravDataProvider) entity).gravityShifter$getGravityDirection();
        return direction != null ? direction : Direction.DOWN;
    }

    public static void setGravity(@NotNull Level level, Direction direction) {
        if (level instanceof ServerLevel serverLevel) {
            GravityServerData.get(serverLevel).setGravity(direction);
            ServerNetwork.syncLevelGravity(serverLevel, direction);
        } else if (PlatformData.getEnv().isClient()) {
            ClientGravityStorage.setGravity(direction);
        }
    }

    public static @NotNull Direction getGravity(@NotNull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return GravityServerData.get(serverLevel).getGravity();
        } else if (PlatformData.getEnv().isClient()) {
            return ClientGravityStorage.getGravity();
        } else {
            LogUtils.getLogger().error("Failed to get the gravity direction for level on {}", PlatformData.getEnv());
            return Direction.DOWN; // only in error cases
        }
    }
}
