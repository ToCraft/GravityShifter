package dev.tocraft.gravityshifter;

import com.mojang.logging.LogUtils;
import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.gravityshifter.command.GravityShifterCommand;
import org.slf4j.Logger;

public class GravityShifter {
    public static final String MODID = "gravityshifter";
    private static Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
        CommandEvents.REGISTRATION.register(new GravityShifterCommand());
    }
}
