package com.georggi.zu.handlers;

import static com.georggi.zu.util.Util.canSeeVanish;
import static com.georggi.zu.util.Util.isVanished;
import static com.georggi.zu.util.Util.systemChatMsgHidden;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ChatEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onChatMessage(ServerChatEvent event) {
        if (event.isCanceled() || event.player == null) {
            return;
        }

        if (isVanished(event.player)) {
            event.setCanceled(true);
            event.player.addChatMessage(
                new ChatComponentText(EnumChatFormatting.RED + "You can't use chat while vanished (for now)"));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerAchievement(AchievementEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.entityPlayer instanceof EntityPlayerMP playerMP && systemChatMsgHidden(playerMP)) {
            event.setCanceled(true);
            playerMP.addChatMessage(
                new ChatComponentText(EnumChatFormatting.RED + "You can't use chat while vanished (for now)"));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.isCanceled()) {
            return;
        }

        for (EntityPlayer online_player : event.player.getEntityWorld().playerEntities) {
            EntityPlayerMP playerMP = (EntityPlayerMP) online_player;
            if (canSeeVanish(playerMP) && online_player != event.player) {
                playerMP.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.YELLOW + event.player.getDisplayName() + " joined the game (invisible)"));
            }
        }

        if (event.player instanceof EntityPlayerMP playerMP && systemChatMsgHidden(playerMP)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.isCanceled()) {
            return;
        }

        for (EntityPlayer online_player : event.player.getEntityWorld().playerEntities) {
            EntityPlayerMP playerMP = (EntityPlayerMP) online_player;
            if (canSeeVanish(playerMP) && online_player != event.player) {
                playerMP.addChatMessage(
                    new ChatComponentText(
                        EnumChatFormatting.YELLOW + event.player.getDisplayName() + " left the game (invisible)"));
            }
        }

        if (event.player instanceof EntityPlayerMP playerMP && systemChatMsgHidden(playerMP)) {
            event.setCanceled(true);
        }
    }
}
