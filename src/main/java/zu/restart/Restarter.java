package zu.restart;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import zu.ZU;

public class Restarter {

    private int restartCounter;
    private final int warnBeforeSeconds;
    private final Date expectedRestartTime;
    private static final Set<Integer> notifyTimes = new HashSet<>(
        Arrays.asList(3600, 1800, 900, 600, 300, 120, 60, 30));

    MinecraftServer server = MinecraftServer.getServer();
    public static Restarter autoRestarter;

    public Restarter(int secondsToRestart, int maxWarning) {
        restartCounter = secondsToRestart * 20;
        warnBeforeSeconds = maxWarning;
        expectedRestartTime = new Date(System.currentTimeMillis() + secondsToRestart * 1000L);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        ZU.LOG.info("Registered new restart in {} seconds", secondsToRestart);
    }

    public void abort() {
        FMLCommonHandler.instance()
            .bus()
            .unregister(this);
        server.getConfigurationManager()
            .sendChatMsg(
                new ChatComponentText("Server restart aborted")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE)));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            if (restartCounter > 0) {
                restartCounter--;
                if (restartCounter % 20 == 0) {
                    // Format time from seconds to HH::MM::SS
                    int secondsToRestart = restartCounter / 20;

                    if ((notifyTimes.contains(secondsToRestart) && secondsToRestart < warnBeforeSeconds)
                        || secondsToRestart <= 10) {
                        // Fix time if server was lagging and skipping ticks
                        long currentTime = System.currentTimeMillis() / 50;
                        long expectedTime = expectedRestartTime.getTime() / 50;

                        restartCounter = (int) Math.max(expectedTime - currentTime, 0);

                        String time = String.format(
                            "%02d:%02d:%02d",
                            secondsToRestart / 3600,
                            (secondsToRestart % 3600) / 60,
                            secondsToRestart % 60);

                        IChatComponent message = new ChatComponentText("Server will restart in " + time)
                            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE));

                        server.getConfigurationManager()
                            .sendChatMsg(message);
                    }
                }
            } else {
                server.initiateShutdown();
            }
        }
    }
}
