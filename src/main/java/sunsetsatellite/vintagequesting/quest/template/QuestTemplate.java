package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.item.IItemConvertible;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class QuestTemplate {
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
	protected List<TaskTemplate> tasks = new ArrayList<>();
	protected List<QuestTemplate> preRequisites = new ArrayList<>();
	protected List<RewardTemplate> rewards = new ArrayList<>();

	public QuestTemplate(String name, String description, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.questLogic = questLogic;
		this.taskLogic = taskLogic;
	}

	public int getWidth() {
		return width;
	}

	public QuestTemplate setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public QuestTemplate setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getX() {
		return x;
	}

	public QuestTemplate setX(int x) {
		this.x = x;
		return this;
	}

	public int getY() {
		return y;
	}

	public QuestTemplate setY(int y) {
		this.y = y;
		return this;
	}

	public int getIconSize() {
		return iconSize;
	}

	public QuestTemplate setIconSize(int iconSize) {
		this.iconSize = iconSize;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getTranslatedName() {
		return I18n.getInstance().translateNameKey(name);
	}

	public QuestTemplate setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public String getTranslatedDescription() {
		return I18n.getInstance().translateDescKey(description);
	}

	public QuestTemplate setDescription(String description) {
		this.description = description;
		return this;
	}

	public IItemConvertible getIcon() {
		return icon;
	}

	public QuestTemplate setIcon(IItemConvertible icon) {
		this.icon = icon;
		return this;
	}

	public Logic getQuestLogic() {
		return questLogic;
	}

	public QuestTemplate setQuestLogic(Logic questLogic) {
		this.questLogic = questLogic;
		return this;
	}

	public Logic getTaskLogic() {
		return taskLogic;
	}

	public QuestTemplate setTaskLogic(Logic taskLogic) {
		this.taskLogic = taskLogic;
		return this;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public QuestTemplate setRepeat(boolean repeat) {
		this.repeat = repeat;
		return this;
	}

	public int getRepeatTicks() {
		return repeatTicks;
	}

	public QuestTemplate setRepeatTicks(int repeatTicks) {
		this.repeatTicks = repeatTicks;
		return this;
	}

	public List<TaskTemplate> getTasks() {
		return tasks;
	}

	public QuestTemplate setTasks(List<TaskTemplate> tasks) {
		this.tasks = tasks;
		return this;
	}

	public List<QuestTemplate> getPreRequisites() {
		return preRequisites;
	}

	public QuestTemplate setPreRequisites(List<QuestTemplate> preRequisites) {
		this.preRequisites = preRequisites;
		return this;
	}

	public List<RewardTemplate> getRewards() {
		return rewards;
	}

	public QuestTemplate setRewards(List<RewardTemplate> rewards) {
		this.rewards = rewards;
		return this;
	}

	public Quest getInstance(){
		return new Quest(this);
	}
}
