package dev.tocraft.gravityshifter.network;

import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.gravityshifter.data.ClientGravityStorage;
import dev.tocraft.gravityshifter.api.GravityData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ClientNetwork {
    public static void initialize() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, ServerNetwork.LEVEL_GRAVITY_SYNC, (context, data) -> data.getString("direction").map(Direction::byName).ifPresent(direction -> context.queue(() -> ClientGravityStorage.setGravity(direction))));

        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, ServerNetwork.ENTITY_GRAVITY_SYNC, (context, data) -> {
            Optional<Integer> id = data.getInt("id");
            Optional<Direction> direction = data.getString("direction").map(Direction::byName);
            if (id.isPresent() && direction.isPresent()) {
                context.queue(() -> {
                    Level level = Minecraft.getInstance().level;
                    if (level != null) {
                        Entity entity = level.getEntity(id.get());
                        if (entity instanceof LivingEntity living) {
                            GravityData.setGravity(living, direction.get());
                        }
                    }
                });
            }
        });
    }
}
