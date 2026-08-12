package sunsetsatellite.vintagequesting.core;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.vintagequesting.core.data.ChapterData;
import sunsetsatellite.vintagequesting.core.data.QuestData;
import sunsetsatellite.vintagequesting.util.QuestTeam;

import java.util.*;

public class Chapter {
	public ChapterData data;
	public QuestTeam team;
	public final Map<QuestData, Quest> entryMap = new HashMap<>();
	public final List<Quest> questList = new ArrayList<>();

	public @NotNull String getName() {
		return data.getName();
	}

	public @NotNull String getDescription() {
		return data.getDescription();
	}

	public Chapter(ChapterData data, QuestTeam team) {
		this.data = data;
		this.team = team;
		data.questList.forEach(this::addQuest);
	}

	public @Nullable Quest getQuest(QuestData quest) {
		return entryMap.get(quest);
	}

	public boolean hasQuest(QuestData quest) {
		return entryMap.containsKey(quest);
	}

	public Quest addQuest(QuestData data) {
		Quest quest = new Quest(data, this);
		entryMap.put(data, quest);
		questList.add(quest);
		return quest;
	}

	public @NotNull List<Quest> getQuests() {
		return questList;
	}

	public @NotNull List<QuestData> getQuestData() {
		return data.getQuestList();
	}

	public @NotNull ItemStack getIcon() {
		return data.getIcon();
	}

	public Quest getStartingQuest() {
		return getQuest(data.getStartingQuest());
	}

	public String getId() {
		return data.id;
	}

	public int getOrderId() {
		return data.orderId;
	}

	public void reset(){
		Set<QuestData> quests = new HashSet<>(entryMap.keySet());
		questList.clear();
		entryMap.clear();
		for (QuestData quest : quests) {
			addQuest(quest);
		}
		for (Quest quest : getQuests()) {
			quest.setupPrerequisites(team);
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
