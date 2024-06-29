package sunsetsatellite.vintagequesting.registry;

import net.minecraft.core.data.registry.Registry;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.Objects;

public class QuestRegistry extends Registry<QuestTemplate> {

	@Override
	public void register(String key, QuestTemplate item) {
		if(!Objects.equals(item.getId(), key)){
			throw new IllegalArgumentException("Identifiers don't match!");
		}
		super.register(key, item);
	}
}
