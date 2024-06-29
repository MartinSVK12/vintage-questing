package sunsetsatellite.vintagequesting.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;

import java.util.Objects;

public class ChapterRegistry extends Registry<ChapterTemplate> {

	@Override
	public void register(String key, ChapterTemplate item) {
		if(!Objects.equals(item.getId(), key)){
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}
}
