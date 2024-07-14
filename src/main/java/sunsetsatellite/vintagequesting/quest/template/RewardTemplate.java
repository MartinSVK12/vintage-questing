package sunsetsatellite.vintagequesting.quest.template;

import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.quest.Reward;

public abstract class RewardTemplate {

	protected final String id;

	protected RewardTemplate(String id) {
		this.id = id;
		VintageQuesting.REWARDS.register(id,this);
	}

	public String getId() {
		return id;
	}

	public abstract Reward getInstance();

	public abstract Reward getInstanceUnique();

    public abstract void reset();
}
