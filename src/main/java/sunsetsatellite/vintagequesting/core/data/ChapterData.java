package sunsetsatellite.vintagequesting.core.data;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Quest;

import java.util.ArrayList;
import java.util.List;

public abstract class ChapterData {
	public final List<QuestData> questList = new ArrayList<>();

	public final String id;
	public final int orderId;

	public abstract @NotNull String getName();

	public abstract @NotNull String getDescription();

	public ChapterData(String id) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = 0;
	}

	public ChapterData(String id, int orderId) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = orderId;
	}

	public void addQuest(@NotNull QuestData quest) {
		questList.add(quest);
	}

	public boolean hasQuest(QuestData quest) {
		return questList.contains(quest);
	}

	public List<QuestData> getQuestList() {
		return questList;
	}

	public abstract @NotNull ItemStack getIcon();

	public abstract QuestData getStartingQuest();

	public String getId() {
		return id;
	}

	public int getOrderId() {
		return orderId;
	}

}
