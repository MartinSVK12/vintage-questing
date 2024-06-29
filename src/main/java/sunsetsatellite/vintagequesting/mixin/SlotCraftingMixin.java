package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.client.entity.player.EntityOtherPlayerMP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.CraftingTask;

@Mixin(value = SlotCrafting.class,remap = false)
public class SlotCraftingMixin {

	@Shadow
	private EntityPlayer thePlayer;

	@Inject(method = "onPickupFromSlot",at = @At("TAIL"))
	public void onPickupFromSlot(ItemStack itemStack, CallbackInfo ci) {
		for (Chapter chapter : ((IHasQuests) thePlayer).getQuestGroup().chapters) {
			for (Quest quest : chapter.getQuests()) {
				for (Task task : quest.getTasks()) {
					if(task instanceof CraftingTask){
						((CraftingTask) task).addProgress(itemStack);
					}
				}
				quest.isCompleted();
			}
		}
	}
}
