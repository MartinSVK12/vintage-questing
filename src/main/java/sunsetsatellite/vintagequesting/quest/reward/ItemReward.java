package sunsetsatellite.vintagequesting.quest.reward;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Reward;

public class ItemReward extends Reward {

	private final ItemStack item;

	public ItemReward(ItemStack stack) {
		this.item = stack;
	}

	public ItemStack getItem() {
		return item.copy();
	}

	@Override
	public void give(EntityPlayer player) {
		if(!redeemed){
			ItemStack stack = item.copy();
			player.inventory.insertItem(stack,true);
			if(stack.stackSize > 0){
				player.dropPlayerItem(stack);
			}
			redeemed = true;
		}
	}
}
