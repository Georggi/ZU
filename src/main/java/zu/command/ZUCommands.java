package zu.command;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import zu.ZU;

public class ZUCommands {

    public static void registerCommands(FMLServerStartingEvent event) {
        boolean isServer = event.getServer()
            .isDedicatedServer();
        if (true) { // TODO: config
            ZU.LOG.debug("Registered Vanish command");
            event.registerServerCommand(new CmdVanish());
        }
        if (isServer) {
            ZU.LOG.debug("Registered Restart command");
            event.registerServerCommand(new CmdRestart());
        }
    }
}
