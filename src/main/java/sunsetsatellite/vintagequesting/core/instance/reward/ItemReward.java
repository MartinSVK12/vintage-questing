package sunsetsatellite.vintagequesting.core.instance.reward;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.reward.ItemRewardData;

public class ItemReward extends Reward {

	private final ItemStack item;

	public ItemReward(ItemRewardData template) {
		super(template);
		this.item = template.getStack();
	}

	public ItemStack getStack() {
		return item.copy();
	}

	@Override
	public void give(Player player) {
		if (!redeemed) {
			ItemStack stack = item.copy();
			player.inventory.insertItem(stack, true);
			if (stack.stackSize > 0) {
				player.dropPlayerItem(stack);
			}
			redeemed = true;
		}
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		super.readFromNbt(nbt);
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		super.writeToNbt(nbt);
	}
}
