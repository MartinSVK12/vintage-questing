package sunsetsatellite.vintagequesting.quest.template.reward;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

public class ItemRewardTemplate extends RewardTemplate {

	protected ItemStack stack;

	public ItemRewardTemplate(String id, ItemStack stack){
        super(id);
        this.stack = stack;
	}

	public ItemStack getStack() {
		return stack;
	}

	@Override
	public Reward getInstance() {
		return new ItemReward(this);
	}
}
