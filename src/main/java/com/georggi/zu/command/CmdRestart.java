package com.georggi.zu.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.georggi.zu.ZUPermissions;
import com.georggi.zu.restart.Restarter;

import serverutils.lib.command.CmdBase;
import serverutils.lib.command.CommandUtils;
import serverutils.lib.data.ForgePlayer;

public class CmdRestart extends CmdBase {

    private static Restarter manualRestarter;

    public CmdRestart() {
        super("restart", Level.OP);
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /restart <seconds|now|abort>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ForgePlayer player = CommandUtils.getForgePlayer(sender);

        if (!player.hasPermission(ZUPermissions.RESTART)) {
            throw new CommandException("commands.generic.permission");
        }

        checkArgs(sender, args, 1);
        MinecraftServer server = getCommandSenderAsPlayer(sender).mcServer;
        switch (args[0]) {
            case "abort":
                if (manualRestarter != null) {
                    manualRestarter.abort();
                    manualRestarter = null;
                } else {
                    throw new CommandException("No restart in progress");
                }
                break;
            case "now":
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "Restarting now"));
                server.initiateShutdown();
                break;
            default:
                if (manualRestarter != null) {
                    throw new CommandException("Restart already in progress");
                }
                int delay = parseInt(sender, args[0]);
                server.getConfigurationManager()
                    .sendChatMsg(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "Server restart initiated"));
                manualRestarter = new Restarter(delay, 3600);
        }
    }
}
