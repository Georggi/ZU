package zu.mixins.vanish;

import static zu.util.Util.canSeeVanish;

import java.util.Set;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import zu.util.LocationResettable;
import zu.util.Vanishable;

@Mixin(value = EntityTracker.class)
@Implements(@Interface(iface = Vanishable.class, prefix = "vanishable$"))
public abstract class MixinEntityTracker {

    @Shadow
    private Set trackedEntities;

    public EntityTrackerEntry findPlayerTracker(EntityPlayerMP player) {
        for (Object o : trackedEntities) {
            EntityTrackerEntry ent = (EntityTrackerEntry) o;
            if (ent.myEntity == player) return ent;
        }

        return null;
    }

    public void hidePlayer(EntityPlayerMP player) {
        EntityTrackerEntry playerTracker = findPlayerTracker(player);
        if (playerTracker != null) {
            for (Object o : trackedEntities) {
                EntityTrackerEntry ent = (EntityTrackerEntry) o;
                if (ent.myEntity instanceof EntityPlayerMP watcher) {
                    if (!canSeeVanish(watcher)) playerTracker.removePlayerFromTracker(watcher);
                }
            }
        }
    }

    public void showPlayer(EntityPlayerMP player) {
        EntityTrackerEntry playerTracker = findPlayerTracker(player);
        if (playerTracker != null) {
            for (Object o : trackedEntities) {
                EntityTrackerEntry ent = (EntityTrackerEntry) o;
                if (ent.myEntity instanceof EntityPlayerMP watcher) {
                    ((LocationResettable) ent).resetTrackerEntryData();
                    playerTracker.tryStartWachingThis(watcher);
                }
            }
        }
    }

}
