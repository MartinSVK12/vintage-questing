package sunsetsatellite.vintagequesting.quest;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class Quest {
	protected String name;
	protected String description;
	protected int x = 0;
	protected int y = 0;
	protected int width = 32;
	protected int height = 32;
	protected int iconSize = 1;
	protected IItemConvertible icon;
	protected Logic questLogic;
	protected Logic taskLogic;
	protected boolean repeat;
	protected int repeatTicks;
	protected List<Task> tasks = new ArrayList<>();
	protected List<Quest> preRequisites = new ArrayList<>();
	protected List<Reward> rewards = new ArrayList<>();

	public Quest(String name, String description, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.questLogic = questLogic;
		this.taskLogic = taskLogic;
	}

	public int getWidth() {
		return width;
	}

	public Quest setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public Quest setHeight(int height) {
		this.height = height;
		return this;
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
		switch (taskLogic){
			case AND:
				return tasks.stream().allMatch(Task::isCompleted);
			case OR:
				return tasks.stream().anyMatch(Task::isCompleted);
		}
		return false;
	}

	public boolean preRequisitesCompleted(){
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
}
