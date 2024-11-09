package zu.mixins.vanish;

import static zu.util.Util.canSeeVanish;
import static zu.util.Util.isVanished;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import zu.util.LocationResettable;

@Mixin(value = EntityTrackerEntry.class)
@Implements(@Interface(iface = LocationResettable.class, prefix = "locR$"))
public abstract class MixinNetEntityTrackerEntry {

    @Shadow
    public Entity myEntity;
    @Shadow
    public boolean isDataInitialized;

    public void resetTrackerEntryData() {
        this.isDataInitialized = false;
    }

    @Inject(
        method = "tryStartWachingThis",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/EntityTrackerEntry;trackingPlayers:Ljava/util/Set;",
            opcode = Opcodes.GETFIELD,
            ordinal = 1,
            shift = At.Shift.BEFORE,
            by = 1),
        cancellable = true)
    private void onTryStartWachingThis(CallbackInfo ci, @Local(name = "arg1", argsOnly = true) EntityPlayerMP player) {
        if (this.myEntity instanceof EntityPlayerMP && isVanished((EntityPlayerMP) this.myEntity)
            && !canSeeVanish(player)) {
            ci.cancel();
        }
    }
}
