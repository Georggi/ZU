package com.georggi.zu.mixins.early.vanish;

import static com.georggi.zu.util.Util.canSeeVanish;
import static com.georggi.zu.util.Util.isVanished;

import java.util.ArrayList;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandListPlayers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.management.ServerConfigurationManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(value = CommandListPlayers.class)
public class MixinCommandListPlayers {

    @Redirect(
        method = "processCommand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/management/ServerConfigurationManager;func_152609_b(Z)Ljava/lang/String;"))
    public String processCommand$func_152609_b(ServerConfigurationManager scm, boolean includeUuid,
        @Local(name = "sender") ICommandSender sender) {
        StringBuilder s = new StringBuilder();
        ArrayList<EntityPlayerMP> arraylist = Lists.newArrayList(scm.playerEntityList);

        boolean senderHasSeeVanish = (sender instanceof EntityPlayerMP playerMP && canSeeVanish(playerMP))
            || (sender instanceof RConConsoleSource);

        for (int i = 0; i < arraylist.size(); ++i) {
            if (isVanished(arraylist.get(i)) && !senderHasSeeVanish) {
                continue;
            }

            if (i > 0) {
                s.append(", ");
            }

            if (isVanished(arraylist.get(i))) {
                s.append("§7(Vanished)§r ");
            }
            s.append((arraylist.get(i)).getCommandSenderName());

            if (includeUuid) {
                s.append(" (")
                    .append(
                        (arraylist.get(i)).getUniqueID()
                            .toString())
                    .append(")");
            }
        }

        return s.toString();
    }
}
