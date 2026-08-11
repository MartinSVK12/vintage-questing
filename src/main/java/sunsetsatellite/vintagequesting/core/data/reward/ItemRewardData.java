package sunsetsatellite.vintagequesting.core.data.reward;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.RewardData;
import sunsetsatellite.vintagequesting.core.instance.reward.ItemReward;


public class ItemRewardData extends RewardData {

	protected ItemStack stack;
	protected Reward cache;

	public ItemRewardData(String id, ItemStack stack) {
		super(id);

		this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Reward getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	@Override
	public Reward getInstanceUnique() {
		return new ItemReward(this);
	}

	@Override
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.reward.vq.item";
	}
}
