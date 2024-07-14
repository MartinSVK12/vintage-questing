package sunsetsatellite.vintagequesting.quest.template.reward;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.reward.ChoiceItemReward;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

import java.util.List;

public class ChoiceItemRewardTemplate extends RewardTemplate {

	protected List<ItemStack> stacks;
	protected Reward cache;

	public ChoiceItemRewardTemplate(String id, List<ItemStack> stacks){
        super(id);

        this.stacks = stacks;
	}

	public List<ItemStack> getStacks() {
		return stacks;
	}

	@Override
	public Reward getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	@Override
	public Reward getInstanceUnique() {
		return new ChoiceItemReward(this);
	}

	@Override
	public void reset() {
		cache = null;
	}
}
