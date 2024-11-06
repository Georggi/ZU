package zu.mixins.vanish;

import static zu.util.Util.isVanished;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityLivingBase.class, remap = false)
public class MixinEntityLivingBase {

    // protected void updateFallState(double p_70064_1_, boolean p_70064_3_)
    // this.worldObj.playAuxSFX(2006, i, j, k, MathHelper.ceiling_float_int(this.fallDistance - 3.0F));
    @Redirect(
        method = "Lnet/minecraft/entity/EntityLivingBase;updateFallState",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playAuxSFX(IIIII)V", ordinal = 0))
    private void updateFallState$playAusSFX(World world, int p_72926_1_, int p_72926_2_, int p_72926_3_, int p_72926_4_,
        int p_72926_5_) {
        EntityLivingBase ent = (EntityLivingBase) (Object) this;

        if (!(ent instanceof EntityPlayerMP player) || !isVanished(player)) {
            ent.worldObj.playAuxSFX(p_72926_1_, p_72926_2_, p_72926_3_, p_72926_4_, p_72926_5_);
        }
    }
}
