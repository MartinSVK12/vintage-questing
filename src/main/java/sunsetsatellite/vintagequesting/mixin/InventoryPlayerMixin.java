package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;

@Mixin(value = InventoryPlayer.class,remap = false)
public class InventoryPlayerMixin {

	@Unique
	InventoryPlayer thisAs = (InventoryPlayer) ((Object)this);

	@Shadow
	public EntityPlayer player;

	@Inject(method = "onInventoryChanged",at = @At("HEAD"))
	public void onInventoryChanged(CallbackInfo ci){
		VintageQuesting.checkQuestCompletion(player);
	}

}
