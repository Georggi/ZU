package com.georggi.zu.handlers;

import static com.georggi.zu.util.Util.isVanished;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.ServerChatEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
}
