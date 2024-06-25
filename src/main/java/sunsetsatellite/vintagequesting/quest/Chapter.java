package sunsetsatellite.vintagequesting.quest;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.gui.GuiQuestButton;

import java.util.List;

public class Chapter {

	protected IItemConvertible icon;
	protected String name;
	protected String description;
	protected List<Quest> quests;

	public Chapter(IItemConvertible icon, String name, String description, List<Quest> quests) {
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

	public List<Quest> getQuests() {
		return quests;
	}
}
