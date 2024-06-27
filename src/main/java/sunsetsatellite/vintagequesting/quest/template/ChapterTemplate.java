package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;

import java.util.List;

public class ChapterTemplate {

	protected IItemConvertible icon;
	protected String name;
	protected String description;
	protected List<QuestTemplate> quests;

	public ChapterTemplate(IItemConvertible icon, String name, String description, List<QuestTemplate> quests) {
		this.icon = icon;
		this.name = name;
		this.description = description;
		this.quests = quests;
	}

	public IItemConvertible getIcon() {
		return icon;
	}

	public String getName() {
		return name;
	}

	public String getTranslatedName(){
		return I18n.getInstance().translateNameKey(name);
	}

	public String getDescription() {
		return description;
	}

	public String getTranslatedDescription(){
		return I18n.getInstance().translateDescKey(description);
	}

	public List<QuestTemplate> getQuests() {
		return quests;
	}

	public Chapter getInstance(){
		return new Chapter(this);
	}
}
