package sunsetsatellite.vintagequesting.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

import java.util.Objects;

public class TaskRegistry extends Registry<TaskTemplate> {

	@Override
	public void register(String key, TaskTemplate item) {
		if(!Objects.equals(item.getId(), key)){
			throw new IllegalArgumentException("Identifiers don't match! "+item.getId()+" != "+key);
		}
		super.register(key, item);
	}
}
