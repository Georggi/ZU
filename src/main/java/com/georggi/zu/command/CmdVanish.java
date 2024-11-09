package com.georggi.zu.command;

import static com.georggi.zu.util.Util.unVanishPlayer;
import static com.georggi.zu.util.Util.vanishPlayer;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import serverutils.lib.command.CmdBase;
import serverutils.lib.command.CommandUtils;
import serverutils.lib.data.ForgePlayer;
import serverutils.lib.data.Universe;
import serverutils.lib.util.NBTUtils;
import com.georggi.zu.ZUPermissions;

public class CmdVanish extends CmdBase {

    public CmdVanish() {
        super("vanish", Level.OP);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ForgePlayer player = CommandUtils.getForgePlayer(sender);

        if (!player.hasPermission(ZUPermissions.VANISH)) {
            throw new CommandException("commands.generic.permission");
        }

        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
        NBTTagCompound nbt = NBTUtils.getPersistedData(playerMP, true);
        boolean isVanished = nbt.getBoolean("vanished");

        if (!isVanished) {
            playerMP.addChatMessage(new ChatComponentText("You are now hidden"));
            vanishPlayer(playerMP);
        } else {
            playerMP.addChatMessage(new ChatComponentText("You are no longer hidden"));
            unVanishPlayer(playerMP);
        }

        nbt.setBoolean("vanished", !isVanished);

        Universe.get()
            .getPlayer(player)
            .clearCache();
    }
}
