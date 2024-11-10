package com.georggi.zu.mixins.late.vanish;

import static com.georggi.zu.util.Util.canSeeVanish;
import static com.georggi.zu.util.Util.systemChatMsgHidden;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import chikachi.discord.listener.MinecraftListener;
import cpw.mods.fml.common.gameevent.PlayerEvent;

@Mixin(value = MinecraftListener.class, remap = false)
public class MixinChikachiMinecraftListener {

    @Inject(method = "onPlayerJoin", at = @At("HEAD"), cancellable = true)
    public void onPlayerJoin(CallbackInfo ci,
        @Local(argsOnly = true, name = "arg1") PlayerEvent.PlayerLoggedInEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.player instanceof EntityPlayerMP playerMP && systemChatMsgHidden(playerMP)) {
            for (EntityPlayer onlinePlayer : event.player.getEntityWorld().playerEntities) {
                EntityPlayerMP onlinePlayerMP = (EntityPlayerMP) onlinePlayer;
                if (canSeeVanish(onlinePlayerMP) && onlinePlayer != event.player) {
                    onlinePlayerMP.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.YELLOW + event.player.getDisplayName()
                                + " joined the game (invisible)"));
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "onPlayerLeave", at = @At("HEAD"), cancellable = true)
    public void onPlayerLeave(CallbackInfo ci,
        @Local(argsOnly = true, name = "arg1") PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.isCanceled()) {
            return;
        }

        if (event.player instanceof EntityPlayerMP playerMP && systemChatMsgHidden(playerMP)) {
            for (EntityPlayer onlinePlayer : event.player.getEntityWorld().playerEntities) {
                EntityPlayerMP onlinePlayerMP = (EntityPlayerMP) onlinePlayer;
                if (canSeeVanish(onlinePlayerMP) && onlinePlayer != event.player) {
                    onlinePlayerMP.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.YELLOW + event.player.getDisplayName() + " left the game (invisible)"));
                }
            }
            ci.cancel();
        }
    }
}
