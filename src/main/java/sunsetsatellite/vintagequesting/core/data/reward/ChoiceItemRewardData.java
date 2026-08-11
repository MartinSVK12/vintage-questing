package sunsetsatellite.vintagequesting.core.data.reward;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.RewardData;
import sunsetsatellite.vintagequesting.core.instance.reward.ChoiceItemReward;

import java.util.List;

public class ChoiceItemRewardData extends RewardData {

	protected List<ItemStack> stacks;
	protected Reward cache;

	public ChoiceItemRewardData(String id, List<ItemStack> stacks) {
		super(id);
		this.stacks = stacks;
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public Reward getInstance() {
		return cache == null ? (cache = getInstanceUnique()) : cache;
	}

	@Override
	public Reward getInstanceUnique() {
		return new ChoiceItemReward(this);
	}

	@Override
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.reward.vq.choiceItem";
	}
}
