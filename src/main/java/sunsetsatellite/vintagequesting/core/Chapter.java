package sunsetsatellite.vintagequesting.core;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.*;

public abstract class Chapter {
	public final Map<QuestData, Quest> entryMap = new HashMap<>();
	public final List<Quest> questList = new ArrayList<>();

	public final String id;
	public final int orderId;

	public abstract @NotNull String getName();

	public abstract @NotNull String getDescription();

	public Chapter(String id) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = 0;
	}

	public Chapter(String id, int orderId) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = orderId;
	}

	public void addQuest(@NotNull QuestData quest) {
		Quest entry = new Quest(quest, this);
		questList.add(entry);
		entryMap.put(quest, entry);
	}

	public @Nullable Quest getQuest(QuestData quest) {
		return entryMap.get(quest);
	}

	public boolean hasQuest(QuestData quest) {
		return entryMap.containsKey(quest);
	}

	public @NotNull List<Quest> getQuests() {
		return questList;
	}

	public @NotNull Set<QuestData> getQuestData() {
		return entryMap.keySet();
	}

	public abstract @NotNull ItemStack getIcon();

	public abstract Quest getStartingQuest();

	public String getId() {
		return id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void reset(){
		Set<QuestData> quests = new HashSet<>(entryMap.keySet());
		questList.clear();
		entryMap.clear();
		for (QuestData quest : quests) {
			addQuest(quest);
		}
		for (Quest quest : getQuests()) {
			quest.setupPrerequisites();
		}
	}

	public double getCompletionFraction() {
		int completed = 0;
		for (Quest q : questList) {
			if (q.isCompleted()) {
				completed++;
			}
		}
		return completed / (double) questList.size();
	}
}
