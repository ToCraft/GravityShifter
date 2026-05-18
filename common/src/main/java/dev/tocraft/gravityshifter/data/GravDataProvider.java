package dev.tocraft.gravityshifter.data;

import net.minecraft.core.Direction;

public interface GravDataProvider {
    void gravityShifter$setGravityDirection(Direction direction);
    Direction gravityShifter$getGravityDirection();
}
