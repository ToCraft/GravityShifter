package dev.tocraft.gravityshifter.neoforge;
import dev.tocraft.gravityshifter.GravityShifter;
import dev.tocraft.gravityshifter.api.GravityData;
import dev.tocraft.gravityshifter.network.ServerNetwork;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod(GravityShifter.MODID)
public class GravityShifterNeoForge {
    public GravityShifterNeoForge() {
        GravityShifter.initialize();

        NeoForge.EVENT_BUS.addListener(GravityShifterNeoForge::onStartTracking);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.@NotNull StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof LivingEntity living) {
            Direction direction = GravityData.getGravity(living);
            ServerNetwork.syncEntityGravity(living, player.level(), direction);
        }
    }
}
