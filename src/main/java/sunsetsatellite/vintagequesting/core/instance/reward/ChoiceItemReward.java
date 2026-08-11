package sunsetsatellite.vintagequesting.core.instance.reward;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.reward.ChoiceItemRewardData;

import java.util.List;

public class ChoiceItemReward extends Reward {

	private ItemStack chosen;
	private final List<ItemStack> stacks;

	public ChoiceItemReward(ChoiceItemRewardData template) {
		super(template);
		this.stacks = template.getStacks();
	}

	public ItemStack getChosenStack() {
		if (chosen == null) return null;
		return chosen.copy();
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	public ItemStack getOption(int index) {
		return stacks.get(index);
	}

	public void choose(int index) {
		chosen = stacks.get(index);
	}

	@Override
	public void give(Player player) {
		if (chosen == null) return;
		if (!redeemed) {
			ItemStack stack = chosen.copy();
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
