package sunsetsatellite.vintagequesting.client;

import net.minecraft.client.util.dispatch.Dispatcher;
import sunsetsatellite.vintagequesting.client.render.RewardRenderer;
import sunsetsatellite.vintagequesting.client.render.reward.ChoiceItemRewardRenderer;
import sunsetsatellite.vintagequesting.client.render.reward.ItemRewardRenderer;
import sunsetsatellite.vintagequesting.client.render.reward.ScoreRewardRenderer;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.instance.reward.ChoiceItemReward;
import sunsetsatellite.vintagequesting.core.instance.reward.ItemReward;
import sunsetsatellite.vintagequesting.core.instance.reward.ScoreReward;

public class RewardRendererDispatcher extends Dispatcher<Class<? extends Reward>, RewardRenderer<? extends Reward>> {

	private static RewardRendererDispatcher instance = new RewardRendererDispatcher();

	private RewardRendererDispatcher() {
		reload();
	}

	public static RewardRendererDispatcher getInstance() {
		return instance;
	}

	public void reload(){
		dispatches.clear();
		addDispatch(ChoiceItemReward.class, new ChoiceItemRewardRenderer());
		addDispatch(ItemReward.class, new ItemRewardRenderer());
		addDispatch(ScoreReward.class, new ScoreRewardRenderer());
	}


	@Override
	protected RewardRenderer<?> getDefault() {
		return null;
	}

	public RewardRenderer<Reward> getDispatch(Class<? extends Reward> aClass) {
		return (RewardRenderer<Reward>) super.getDispatch(aClass);
	}
}
