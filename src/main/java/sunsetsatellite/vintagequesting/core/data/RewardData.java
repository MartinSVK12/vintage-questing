package sunsetsatellite.vintagequesting.core.data;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Reward;

public abstract class RewardData {

	protected final String id;

	protected RewardData(String id) {
		this.id = id;
		VintageQuesting.REWARDS.register(id, this);
	}

	public String getId() {
		return id;
	}

	public abstract Reward getInstance();

	public abstract Reward getInstanceUnique();

	public abstract void clearInstance();

	public abstract String getTypeId();

	public String getTypeName() {
		return Catalyst.translateNameKey(getTypeId());
	}
}
