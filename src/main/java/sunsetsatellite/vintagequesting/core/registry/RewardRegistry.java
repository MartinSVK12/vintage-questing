package sunsetsatellite.vintagequesting.core.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.core.data.RewardData;

import java.util.Objects;

public class RewardRegistry extends Registry<RewardData> {

	@Override
	public void register(String key, RewardData item) {
		if (!Objects.equals(item.getId(), key)) {
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}
}
