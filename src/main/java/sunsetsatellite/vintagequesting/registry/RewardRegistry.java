package sunsetsatellite.vintagequesting.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

import java.util.Objects;

public class RewardRegistry extends Registry<RewardTemplate> {

	@Override
	public void register(String key, RewardTemplate item) {
		if(!Objects.equals(item.getId(), key)){
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}
}
