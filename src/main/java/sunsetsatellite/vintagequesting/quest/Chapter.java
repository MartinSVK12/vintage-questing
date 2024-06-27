package sunsetsatellite.vintagequesting.quest;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.gui.GuiQuestButton;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.ArrayList;
import java.util.List;

public class Chapter {

	protected IItemConvertible icon;
	protected String name;
	protected String description;
	protected List<Quest> quests;

	public Chapter(ChapterTemplate template) {
		this.icon = template.getIcon();
		this.name = template.getName();
		this.description = template.getDescription();
		ArrayList<Quest> list = new ArrayList<>();
		for (QuestTemplate quest : template.getQuests()) {
			list.add(quest.getInstance());
		}
		this.quests = list;
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

	public List<Quest> getQuests() {
		return quests;
	}
}
