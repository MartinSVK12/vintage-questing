package sunsetsatellite.vintagequesting.quest.template.reward;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

public class ItemRewardTemplate extends RewardTemplate {

	protected ItemStack stack;
	protected Reward cache;

	public ItemRewardTemplate(String id, ItemStack stack){
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
	public void reset() {
		cache = null;
	}
}
