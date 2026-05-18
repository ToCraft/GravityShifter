package dev.tocraft.gravityshifter.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.tocraft.gravityshifter.GravityShifter;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;

public class GravityServerData extends SavedData {
    private Direction gravity = Direction.DOWN;

    public static final Codec<GravityServerData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Direction.CODEC.fieldOf("gravity").forGetter(d -> d.gravity)
            ).apply(instance, GravityServerData::new)
    );

    public static final SavedDataType<@NotNull GravityServerData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(GravityShifter.MODID, "gravity"),
            GravityServerData::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    public GravityServerData() {}

    public GravityServerData(@NotNull Direction gravity) {
        this.gravity = gravity;
    }

    public Direction getGravity() { return gravity; }

    public void setGravity(@NotNull Direction gravity) {
        this.gravity = gravity;
        setDirty();
    }

    @NotNull
    public static GravityServerData get(@NotNull ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }
}