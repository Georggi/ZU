package com.georggi.zu.mixins.early.vanish;

import static com.georggi.zu.util.Util.canSeeSysMessages;
import static com.georggi.zu.util.Util.systemChatMsgHidden;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @Redirect(
        method = "onDisconnect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnect_sendChatMsg(ServerConfigurationManager scm, IChatComponent component) {
        NetHandlerPlayServer nps = (NetHandlerPlayServer) (Object) this;
        if (!systemChatMsgHidden(nps.playerEntity)) {
            scm.sendChatMsg(component);
        } else {
            for (EntityPlayerMP player : scm.playerEntityList) {
                if (player != nps.playerEntity && canSeeSysMessages(player)) {
                    player.addChatMessage(component);
                }
            }
        }
    }
}
