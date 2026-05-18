package dev.tocraft.gravityshifter.neoforge;
import dev.tocraft.gravityshifter.GravityShifter;
import net.neoforged.fml.common.Mod;

@SuppressWarnings("unused")
@Mod(GravityShifter.MODID)
public class GravityShifterNeoForge {
    public GravityShifterNeoForge() {
        GravityShifter.initialize();
    }
}
