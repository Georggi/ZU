package com.georggi.zu.command;

import com.georggi.zu.ZU;
import com.georggi.zu.ZUConfig;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ZUCommands {

    public static void registerCommands(FMLServerStartingEvent event) {
        boolean isServer = event.getServer()
            .isDedicatedServer();
        if (ZUConfig.enableVanish) {
            ZU.LOG.debug("Registered Vanish command");
            event.registerServerCommand(new CmdVanish());
        }
        if (isServer) {
            ZU.LOG.debug("Registered Restart command");
            event.registerServerCommand(new CmdRestart());
        }
    }
}
