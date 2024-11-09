package com.georggi.zu;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ZUConfig {

    public static String discordBotToken = "";
    public static String restartSchedule = "";
    public static boolean enableVanish = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        discordBotToken = configuration
            .getString("DiscordToken", Configuration.CATEGORY_GENERAL, discordBotToken, "Token to use for discord bot");
        restartSchedule = configuration.getString(
            "RestartSchedule",
            Configuration.CATEGORY_GENERAL,
            restartSchedule,
            "Restart schedule in cron format");
        enableVanish = configuration
            .getBoolean("EnableVanish", Configuration.CATEGORY_GENERAL, enableVanish, "Enable vanish feature");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
