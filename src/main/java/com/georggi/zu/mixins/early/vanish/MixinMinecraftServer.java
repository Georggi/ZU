package com.georggi.zu.mixins.early.vanish;

import com.georggi.zu.util.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

@Mixin(value = MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    private ServerConfigurationManager serverConfigManager;

    /**
     * @author Georggi
     * @reason Don't return vanished players in player count
     */
    @Overwrite
    public int getCurrentPlayerCount() {
        int vanished_count = 0;
        for (EntityPlayerMP player : serverConfigManager.playerEntityList) {
            if (Util.isVanished(player)) {
                vanished_count++;
            }
        }
        return serverConfigManager.playerEntityList.size() - vanished_count;
    }

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;get(I)Ljava/lang/Object;",
            shift = At.Shift.BEFORE,
            by = 1))
    private void tick$filterStatusResponse(CallbackInfo ci, @Local(name = "j") int j, @Local(name = "k") int k) {
        EntityPlayerMP player = serverConfigManager.playerEntityList.get(j + k);
        while (Util.isVanished(player)) {
            ++j;
            player = serverConfigManager.playerEntityList.get(j + k);
        }
    }
}
