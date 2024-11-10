package com.georggi.zu;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.georggi.zu.command.ZUCommands;
import com.georggi.zu.handlers.ChatEventHandler;
import com.georggi.zu.restart.Restarter;
import com.georggi.zu.util.RestartSchedule;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ZU.MODID, name = "Zvezdolet Utilities", version = Tags.VERSION, acceptableRemoteVersions = "*")
public class ZU {

    public static final String MODID = "zu";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.Instance(ZU.MODID)
    public static ZU INSTANCE;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        ZUConfig.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        ZU.LOG.info(ZUConfig.discordBotToken);
        ZU.LOG.info(ZUConfig.restartSchedule);
        ZU.LOG.info("I am " + ZU.MODID + " at version " + Tags.VERSION);

        MinecraftForge.EVENT_BUS.register(new ChatEventHandler());
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        ZUCommands.registerCommands(event);

        if (ZUConfig.restartSchedule != null) {
            try {
                RestartSchedule schedule = new RestartSchedule(ZUConfig.restartSchedule);
                if (schedule.secondsToRestart > 0) {
                    Restarter.autoRestarter = new Restarter(schedule.secondsToRestart, 3600);
                }
            } catch (Exception e) {
                ZU.LOG.error("Failed to parse restart schedule", e);
            }
        }
    }
}
