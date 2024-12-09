package com.georggi.zu.mixins.interfaces;

import net.minecraft.command.ICommandSender;

public interface BetterGetAllUsernames {

    String[] getAllUsernames(ICommandSender sender);
}
