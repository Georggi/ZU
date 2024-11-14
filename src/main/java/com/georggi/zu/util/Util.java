package com.georggi.zu.util;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;

import com.georggi.zu.ZUPermissions;
import com.georggi.zu.mixins.interfaces.Vanishable;

import serverutils.lib.command.CommandUtils;
import serverutils.lib.data.ForgePlayer;
import serverutils.lib.util.NBTUtils;

public class Util {

    public static boolean isVanished(EntityPlayerMP player) {
        NBTTagCompound nbt = NBTUtils.getPersistedData(player, true);
        return nbt.getBoolean("vanished");
    }

    public static boolean systemChatMsgHidden(EntityPlayerMP player) {
        try {
            ForgePlayer forgePlayer = CommandUtils.getForgePlayer(player);
            return forgePlayer.hasPermission(ZUPermissions.HIDE_SYS_MESSAGES);
        } catch (CommandException e) {
            return true; // Hide stupid bugged messages
        }
    }

    public static boolean canSeeVanish(EntityPlayerMP player) {
        ForgePlayer forgePlayer = CommandUtils.getForgePlayer(player);
        return forgePlayer.hasPermission(ZUPermissions.SEE_VANISH);
    }

    public static void sendPlayerPacketToAllowedPlayers(ServerConfigurationManager scm, Packet packetIn,
        EntityPlayerMP packetPlayer) {
        for (EntityPlayerMP player : scm.playerEntityList) {
            if (canSeeVanish(player) || player == packetPlayer) {
                player.playerNetServerHandler.sendPacket(packetIn);
            }
        }
    }

    public static void vanishFilter$sendPacketToAllPlayers(ServerConfigurationManager scm,
        EntityPlayerMP entityplayermp, Packet packetIn) {
        if (isVanished(entityplayermp)) {
            sendPlayerPacketToAllowedPlayers(scm, packetIn, entityplayermp);
        } else {
            scm.sendPacketToAllPlayers(packetIn);
        }
    }

    public static void vanishFilter$sendPacket(NetHandlerPlayServer nps, EntityPlayerMP joiner, EntityPlayerMP target,
        Packet packetIn) {
        if (!isVanished(target) || canSeeVanish(joiner) || target == joiner) {
            nps.sendPacket(packetIn);
        }
    }

    public static void vanishPlayer(EntityPlayerMP player) {
        S38PacketPlayerListItem tablistPacket = new S38PacketPlayerListItem(player.getCommandSenderName(), false, 9999);
        ((Vanishable) (((WorldServer) player.worldObj).getEntityTracker())).hidePlayer(player);
        for (EntityPlayerMP packetPlayer : player.mcServer.getConfigurationManager().playerEntityList) {
            if (!canSeeVanish(player) && player != packetPlayer) {
                packetPlayer.playerNetServerHandler.sendPacket(tablistPacket);
            }
        }
    }

    public static void unVanishPlayer(EntityPlayerMP player) {
        S38PacketPlayerListItem tablistPacket = new S38PacketPlayerListItem(player.getCommandSenderName(), true, 1000);
        ((Vanishable) (((WorldServer) player.worldObj).getEntityTracker())).showPlayer(player);
        for (EntityPlayerMP packetPlayer : player.mcServer.getConfigurationManager().playerEntityList) {
            if (!canSeeVanish(player) && player != packetPlayer) {
                packetPlayer.playerNetServerHandler.sendPacket(tablistPacket);
            }
        }
    }
}
