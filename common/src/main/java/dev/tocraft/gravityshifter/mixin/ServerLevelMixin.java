package dev.tocraft.gravityshifter.mixin;

import dev.tocraft.gravityshifter.data.GravityServerData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "save", at = @At("HEAD"))
    private void onSave(ProgressListener progressListener, boolean flush, boolean skip, CallbackInfo ci) {
        ServerLevel level = (ServerLevel)(Object)this;
        level.getDataStorage().computeIfAbsent(GravityServerData.TYPE);
        level.getDataStorage().set(GravityServerData.TYPE, GravityServerData.get(level));
    }
}
