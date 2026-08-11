package sunsetsatellite.vintagequesting.core.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.core.Chapter;

import java.util.Objects;

public class ChapterRegistry extends Registry<Chapter> {
	/*@Override
	public void register(String key, Chapter item) {
		if (!Objects.equals(item.getId(), key)) {
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}*/
}
