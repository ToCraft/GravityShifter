package dev.tocraft.gravityshifter.network;

import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.gravityshifter.GravityShifter;
import dev.tocraft.gravityshifter.mixin.ChunkMapAccessor;
import dev.tocraft.gravityshifter.mixin.EntityTrackerAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ServerNetwork {
    public static final Identifier LEVEL_GRAVITY_SYNC = GravityShifter.id("level_gravity");
    public static final Identifier ENTITY_GRAVITY_SYNC = GravityShifter.id("entity_gravity");

    public static void initialize() {
        ModernNetworking.registerType(LEVEL_GRAVITY_SYNC);
        ModernNetworking.registerType(ENTITY_GRAVITY_SYNC);
    }

    public static void syncLevelGravity(@NotNull ServerLevel level, Direction direction) {
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            sendLevelGravitySync(player, direction);
        }
    }

    public static void syncEntityGravity(@NotNull LivingEntity entity, @NotNull ServerLevel level, Direction direction) {
        if (entity instanceof ServerPlayer to) {
            sendEntityGravitySync(to, to, direction);
        }
        Int2ObjectMap<Object> trackers = ((ChunkMapAccessor) level
                .getChunkSource().chunkMap).getEntityMap();
        Object tracking = trackers.get(entity.getId());
        ((EntityTrackerAccessor) tracking).getSeenBy().forEach(
                listener -> sendEntityGravitySync(listener.getPlayer(), entity, direction));
    }

    public static void sendLevelGravitySync(ServerPlayer to, @NotNull Direction direction) {
        CompoundTag tag = new CompoundTag();
        tag.putString("direction", direction.getName());
        ModernNetworking.sendToPlayer(to, LEVEL_GRAVITY_SYNC, tag);
    }

    public static void sendEntityGravitySync(ServerPlayer to, @NotNull LivingEntity entity, @NotNull Direction direction) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", entity.getId());
        tag.putString("direction", direction.getName());
        ModernNetworking.sendToPlayer(to, ENTITY_GRAVITY_SYNC, tag);
    }
}
