package sunsetsatellite.vintagequesting.core.data.reward;


import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.RewardData;
import sunsetsatellite.vintagequesting.core.instance.reward.ScoreReward;

public class ScoreRewardData extends RewardData {

	protected int amount;
	protected Reward cache;

	public ScoreRewardData(String id, int amount) {
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
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.reward.vq.score";
	}
}
