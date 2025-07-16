package sunsetsatellite.vintagequesting.quest;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.gui.QuestToast;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class Quest {
	protected String name;
	protected String description;
	protected int x;
	protected int y;
	protected int iconSize;
	protected IItemConvertible icon;
	protected Logic questLogic;
	protected Logic taskLogic;
	protected boolean repeat;
	protected int repeatTicks;
	protected List<Task> tasks;
	protected List<Quest> preRequisites;
	protected List<Reward> rewards;
	protected final QuestTemplate template;
	protected boolean complete;
	protected QuestChapterPage page;

	public Quest(QuestTemplate template, QuestChapterPage page) {
		this.template = template;
		this.name = template.getName();
		this.description = template.getDescription();
		this.x = template.getX();
		this.y = template.getY();
		this.icon = template.getIcon();
		this.iconSize = 1;
		this.repeat = template.isRepeat();
		this.repeatTicks = template.getRepeatTicks();
		this.questLogic = template.getQuestLogic();
		this.taskLogic = template.getTaskLogic();
		ArrayList<Task> taskList = new ArrayList<>();
		ArrayList<Quest> preRequisiteList = new ArrayList<>();
		ArrayList<Reward> rewardList = new ArrayList<>();
		for (TaskTemplate task : template.getTasks()) {
			taskList.add(task.getInstance());
		}
        for (RewardTemplate reward : template.getRewards()) {
            rewardList.add(reward.getInstance());
        }
        this.tasks = taskList;
        this.preRequisites = preRequisiteList;
        this.rewards = rewardList;
	}

	public void setupPrerequisites(){
		this.preRequisites = new ArrayList<>();
		for (QuestTemplate preRequisite : template.getPreRequisites()) {
			for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
				Quest quest = chapter.getQuest(preRequisite);
				if(quest != null){
					this.preRequisites.add(quest);
				}
			}
		}
	}

	public QuestChapterPage getPage() {
		return page;
	}

	public int getX() {
		return x;
	}

	public Quest setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public Quest setY(int y) {
		this.y = y;
		return this;
	}

	public int getIconSize() {
		return iconSize;
	}

	public Quest setIconSize(int iconSize) {
		this.iconSize = iconSize;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey(name);
	}

	public Quest setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public String getTranslatedDescription() {
		return I18n.getInstance().translateDescKey(description);
	}

	public Quest setDescription(String description) {
		this.description = description;
		return this;
	}

	public IItemConvertible getIcon() {
		return icon;
	}

	public Quest setIcon(IItemConvertible icon) {
		this.icon = icon;
		return this;
	}

	public Logic getQuestLogic() {
		return questLogic;
	}

	public Quest setQuestLogic(Logic questLogic) {
		this.questLogic = questLogic;
		return this;
	}

	public Logic getTaskLogic() {
		return taskLogic;
	}

	public Quest setTaskLogic(Logic taskLogic) {
		this.taskLogic = taskLogic;
		return this;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public Quest setRepeat(boolean repeat) {
		this.repeat = repeat;
		return this;
	}

	public int getRepeatTicks() {
		return repeatTicks;
	}

	public Quest setRepeatTicks(int repeatTicks) {
		this.repeatTicks = repeatTicks;
		return this;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Quest setTasks(List<Task> tasks) {
		this.tasks = tasks;
		return this;
	}

	public List<Quest> getPreRequisites() {
		return preRequisites;
	}

	public Quest setPreRequisites(List<Quest> preRequisites) {
		this.preRequisites = preRequisites;
		return this;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public Quest setRewards(List<Reward> rewards) {
		this.rewards = rewards;
		return this;
	}

	public boolean areAllRewardsRedeemed() {
		return getRewards().stream().allMatch(Reward::isRedeemed);
	}

	public boolean isCompleted(){
		if(!preRequisitesCompleted()) return false;
		switch (taskLogic){
			case AND:
				if (tasks.stream().allMatch(Task::isCompleted)){
					if (!complete && !Global.isServer) {
						Minecraft.getMinecraft().guiToasts.addToast(new QuestToast(this));
						complete = true;
					}
					return true;
				}
				else return complete = false;
			case OR:
				if (tasks.stream().anyMatch(Task::isCompleted)){
					if (!complete && !Global.isServer) {
						Minecraft.getMinecraft().guiToasts.addToast(new QuestToast(this));
						complete = true;
					}
					return true;
				}
				else return complete = false;
		}
		return complete = false;
	}

	public boolean preRequisitesCompleted(){
		if(preRequisites.isEmpty()) return true;
		switch (taskLogic){
			case AND:
				return preRequisites.stream().allMatch(Quest::isCompleted);
			case OR:
				return preRequisites.stream().anyMatch(Quest::isCompleted);
		}
		return false;
	}

	public long numberOfCompletedTasks(){
		return tasks.stream().filter(Task::isCompleted).count();
	}

	public QuestTemplate getTemplate() {
		return template;
	}

	public void readFromNbt(CompoundTag nbt) {
		complete = nbt.getBoolean("Completed");
	}

	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Completed", complete);
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
}
