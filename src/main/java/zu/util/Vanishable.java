package zu.util;

import net.minecraft.entity.player.EntityPlayerMP;

public interface Vanishable {

    void hidePlayer(EntityPlayerMP player);

    void showPlayer(EntityPlayerMP player);
}
