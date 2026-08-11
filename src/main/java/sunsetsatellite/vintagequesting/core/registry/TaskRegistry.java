package sunsetsatellite.vintagequesting.core.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.core.data.TaskData;

import java.util.Objects;

public class TaskRegistry extends Registry<TaskData> {

	@Override
	public void register(String key, TaskData item) {
		if (!Objects.equals(item.getId(), key)) {
			throw new IllegalArgumentException("Identifiers don't match! " + item.getId() + " != " + key);
		}
		super.register(key, item);
	}
}
