package zu.mixins.vanish;

import static zu.util.Util.isVanished;
import static zu.util.Util.vanishFilter$sendPacket;
import static zu.util.Util.vanishFilter$sendPacketToAllPlayers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.IChatComponent;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;

// TODO: players with SEE_VANISH permission receive modified username in player list
// TODO: Player himself also receives modified (italics) username in player list
// TODO: Discord
// TODO: JourneyMap (if handles properly then nothing to be done)
// TODO: Morpheus
// TODO: chat

@Mixin(value = ServerConfigurationManager.class)
public class MixinServerConfigurationManager {

    @Shadow
    @Final
    public List<net.minecraft.entity.player.EntityPlayerMP> playerEntityList;

    /**
     * @author Georggi
     * @reason Do not leak vanished players in tab completion
     */
    @Overwrite
    public String[] getAllUsernames() {
        List<EntityPlayerMP> playerEntityList = new ArrayList<>();

        for (EntityPlayerMP player : this.playerEntityList) {
            if (!isVanished(player)) {
                playerEntityList.add(player);
            }
        }

        int playersCount = playerEntityList.size();
        String[] astring = new String[playersCount];
        for (int i = 0; i < playersCount; ++i) {
            astring[i] = (playerEntityList.get(i)).getCommandSenderName();
        }

        return astring;
    }

    @Redirect(
        method = "sendPlayerInfoToAllPlayers",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/network/Packet;)V"))
    public void sendPlayerInfoToAllPlayers_sendPacketToAllPlayers(ServerConfigurationManager scm, Packet packetIn,
        @Local(name = "entityplayermp") EntityPlayerMP entityplayermp) {
        vanishFilter$sendPacketToAllPlayers(scm, entityplayermp, packetIn);
    }

    @Redirect(
        method = "playerLoggedIn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/network/Packet;)V"))
    public void playerLoggedIn_sendPacketToAllPlayers(ServerConfigurationManager scm, Packet packetIn,
        @Local(name = "arg1", argsOnly = true) EntityPlayerMP entityplayermp) {
        vanishFilter$sendPacketToAllPlayers(scm, entityplayermp, packetIn);
    }

    @Redirect(
        method = "playerLoggedIn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/NetHandlerPlayServer;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void playerLoggedIn_sendPacket(NetHandlerPlayServer nps, Packet packetIn,
        @Local(name = "arg1", argsOnly = true) EntityPlayerMP joiner,
        @Local(name = "entityplayermp1") EntityPlayerMP target) {
        vanishFilter$sendPacket(nps, joiner, target, packetIn);
    }

    @Redirect(
        method = "initializeConnectionToPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;sendChatMsg(Lnet/minecraft/util/IChatComponent;)V"))
    public void initializeConnectionToPlayer_sendChatMsg(ServerConfigurationManager scm, IChatComponent component,
        @Local(name = "arg2", argsOnly = true) EntityPlayerMP joiner) {
        if (!isVanished(joiner)) {
            scm.sendChatMsg(component);
        }
    }
}
