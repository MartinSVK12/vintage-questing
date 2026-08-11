package sunsetsatellite.vintagequesting.core;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.IItemConvertible;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.data.QuestData;
import sunsetsatellite.vintagequesting.core.data.RewardData;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class Quest {

	public final QuestData data;
	public Chapter chapter;
	public boolean complete;
	public int repeatTicks;
	public List<Task> tasks;
	public List<Quest> preRequisites;
	public List<Reward> rewards;

	public Quest(QuestData data, Chapter chapter) {
		this.data = data;
		this.chapter = chapter;

		ArrayList<Task> taskList = new ArrayList<>();
		ArrayList<Quest> preRequisiteList = new ArrayList<>();
		ArrayList<Reward> rewardList = new ArrayList<>();
		for (TaskData task : data.getTasks()) {
			taskList.add(task.getInstance());
		}
		for (RewardData reward : data.getRewards()) {
			rewardList.add(reward.getInstance());
		}
		this.tasks = taskList;
		this.preRequisites = preRequisiteList;
		this.rewards = rewardList;
	}

	public void setupPrerequisites() {
		this.preRequisites = new ArrayList<>();
		for (QuestData preRequisite : data.getPreRequisites()) {
			for (Chapter chapter : VintageQuesting.CHAPTERS) {
				Quest quest = chapter.getQuest(preRequisite);
				if (quest != null) {
					this.preRequisites.add(quest);
				}
			}
		}
	}

	public String getTranslatedName() {
		return data.getTranslatedName();
	}

	public String getTranslatedDescription() {
		return data.getTranslatedDescription();
	}

	public IItemConvertible getIcon() {
		return data.getIcon();
	}

	public boolean areAllRewardsRedeemed() {
		return rewards.stream().allMatch(Reward::isRedeemed);
	}

	public boolean isCompleted() {
		if (!preRequisitesCompleted()) return false;
		switch (data.getTaskLogic()) {
			case AND -> {
				if (tasks.stream().allMatch(Task::isCompleted)) {
					if (!complete) {
						complete = true;
					}
					return true;
				} else return complete = false;
			}
			case OR -> {
				if (tasks.stream().anyMatch(Task::isCompleted)) {
					if (!complete) {
						complete = true;
					}
					return true;
				} else return complete = false;
			}
		}
		return complete = false;
	}

	public boolean preRequisitesCompleted() {
		if (preRequisites.isEmpty()) return true;
		return switch (data.getTaskLogic()) {
			case AND -> preRequisites.stream().allMatch(Quest::isCompleted);
			case OR -> preRequisites.stream().anyMatch(Quest::isCompleted);
		};
	}

	public long numberOfCompletedTasks() {
		return tasks.stream().filter(Task::isCompleted).count();
	}

	public void forceComplete() {
		for (Task task : tasks) {
			task.forceComplete();
		}
	}

	public void reset() {
		for (Task task : tasks) {
			task.reset();
		}
		this.complete = false;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public List<Quest> getPreRequisites() {
		return preRequisites;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Logic getTaskLogic() {
		return data.getTaskLogic();
	}

	public Logic getQuestLogic() {
		return data.getQuestLogic();
	}

	public int getY() {
		return data.getY();
	}

	public int getX() {
		return data.getX();
	}

	public void readFromNbt(CompoundTag nbt) {
		complete = nbt.getBoolean("Completed");
	}

	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Completed", complete);
	}
}
