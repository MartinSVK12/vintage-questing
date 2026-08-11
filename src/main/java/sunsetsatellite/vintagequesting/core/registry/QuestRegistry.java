package sunsetsatellite.vintagequesting.core.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.Objects;

public class QuestRegistry extends Registry<QuestData> {

	@Override
	public void register(String key, QuestData item) {
		if (!Objects.equals(item.getId(), key)) {
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}
}
