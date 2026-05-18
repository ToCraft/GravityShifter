package dev.tocraft.gravityshifter.mixin;

import dev.tocraft.gravityshifter.data.GravDataProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class EntityDataMixin implements GravDataProvider {
    @Unique
    private static final String gravityShifter$GRAVITY_TAG_NAME = "GravityDirection";

    @Unique
    private Direction gravityShifter$gravity = null;

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readData(ValueInput input, CallbackInfo ci) {
        Optional<String> str = input.getString(gravityShifter$GRAVITY_TAG_NAME);
        str.ifPresent(s -> this.gravityShifter$gravity = Direction.byName(s));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void writeData(ValueOutput output, CallbackInfo ci) {
        if (gravityShifter$gravity != null) {
            output.putString(gravityShifter$GRAVITY_TAG_NAME, gravityShifter$gravity.name());
        }
    }

    @Override
    public void gravityShifter$setGravityDirection(Direction direction) {
        this.gravityShifter$gravity = direction;
    }

    @Override
    public Direction gravityShifter$getGravityDirection() {
        return gravityShifter$gravity;
    }
}
