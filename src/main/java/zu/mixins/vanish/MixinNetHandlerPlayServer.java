package zu.mixins.vanish;

import static zu.util.Util.isVanished;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = NetHandlerPlayServer.class, remap = false)
public class MixinNetHandlerPlayServer {

    @Redirect(
        method = "onDisconnect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void onDisconnect_sendChatMsg(ServerConfigurationManager scm, IChatComponent component) {
        NetHandlerPlayServer nps = (NetHandlerPlayServer) (Object) this;

        if (!isVanished(nps.playerEntity)) {
            scm.sendChatMsg(component);
        }
    }
}
