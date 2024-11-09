package com.georggi.zu.mixins.early.vanish;

import static com.georggi.zu.util.Util.isVanished;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public class MixinEntity {

    @Redirect(
        method = "moveEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;playSound(Ljava/lang/String;FF)V",
            ordinal = 0))
    private void moveEntity$playSound(Entity ent, String name, float volume, float pitch) {
        if (ent instanceof EntityPlayerMP player && !isVanished(player)) {
            ent.playSound(name, volume, pitch);
        }
    }

    @Redirect(
        method = "handleWaterMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;playSound(Ljava/lang/String;FF)V",
            ordinal = 0))
    private void handleWaterMovement$playSound(Entity ent, String name, float volume, float pitch) {
        if (ent instanceof EntityPlayerMP player && !isVanished(player)) {
            ent.playSound(name, volume, pitch);
        }
    }

    @Inject(method = "func_145780_a", at = @At("HEAD"), cancellable = true)
    private void func_145780_a$cancel(CallbackInfo ci) {
        Entity ent = (Entity) (Object) this;

        if (ent instanceof EntityPlayerMP player && isVanished(player)) {
            ci.cancel();
        }
    }
}
