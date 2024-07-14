package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.quest.Chapter;

import java.util.List;

public class ChapterTemplate {

	protected final String id;
	protected IItemConvertible icon;
	protected String name;
	protected String description;
	protected List<QuestTemplate> quests;
	protected final int order;

	protected Chapter cache;

	public ChapterTemplate(String id, int order, IItemConvertible icon, String name, String description, List<QuestTemplate> quests) {
		this.id = id;
		this.icon = icon;
		this.name = name;
		this.description = description;
		this.quests = quests;
		this.order = order;
		VintageQuesting.CHAPTERS.register(id,this);
	}

	public ChapterTemplate(String id, int order, IItemConvertible icon, String langId, List<QuestTemplate> quests) {
		this.id = id;
		this.icon = icon;
		this.name = langId;
		this.description = langId;
		this.quests = quests;
		this.order = order;
		VintageQuesting.CHAPTERS.register(id,this);
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
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	public Chapter getInstanceUnique(){
		return new Chapter(this);
	}

	public String getId() {
		return id;
	}

	public int getOrder() {
		return order;
	}

	public void reset() {
		cache = null;
	}
}
