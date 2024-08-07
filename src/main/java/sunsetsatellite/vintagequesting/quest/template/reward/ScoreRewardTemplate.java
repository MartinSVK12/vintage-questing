package sunsetsatellite.vintagequesting.quest.template.reward;

import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.reward.ScoreReward;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

public class ScoreRewardTemplate extends RewardTemplate {

	protected int amount;
	protected Reward cache;

	public ScoreRewardTemplate(String id, int amount){
        super(id);

		this.amount = amount;
	}

	public int getScore() {
		return amount;
	}

	@Override
	public Reward getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	@Override
	public Reward getInstanceUnique() {
		return new ScoreReward(this);
	}

	@Override
	public void reset() {
		cache = null;
	}
}
