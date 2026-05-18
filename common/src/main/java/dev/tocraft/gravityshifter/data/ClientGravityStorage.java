package dev.tocraft.gravityshifter.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class ClientGravityStorage {
    private static Direction gravity = Direction.DOWN;

    public static Direction getGravity() { return gravity; }
    public static void setGravity(Direction gravity) {
        ClientGravityStorage.gravity = gravity;
    }
}