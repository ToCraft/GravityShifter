package dev.tocraft.gravityshifter;

import com.mojang.logging.LogUtils;
import dev.tocraft.craftedcore.event.common.ServerLevelEvents;
import org.slf4j.Logger;

public class GravityShifter {
    public static final String MODID = "gravityshifter";
    private static Logger LOGGER = LogUtils.getLogger();

    public static void initialize() {
        ServerLevelEvents.LEVEL_LOAD.register(_ -> LOGGER.info("GravityShifter was loaded!"));
    }
}
