package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.item.IItemConvertible;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class QuestTemplate {
	protected final String id;
	protected String name;
	protected String description;
	protected int x = 0;
	protected int y = 0;
	protected IItemConvertible icon;
	protected Logic questLogic;
	protected Logic taskLogic;
	protected boolean repeat;
	protected int repeatTicks;
	protected List<TaskTemplate> tasks = new ArrayList<>();
	protected List<QuestTemplate> preRequisites = new ArrayList<>();
	protected List<RewardTemplate> rewards = new ArrayList<>();
	public Achievement.Type type = Achievement.TYPE_NORMAL;

	public QuestTemplate(String id, String name, String description, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.questLogic = questLogic;
		this.taskLogic = taskLogic;
		VintageQuesting.QUESTS.register(id, this);
	}

	public QuestTemplate(String id, String langId, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
		this.id = id;
		this.name = langId;
		this.description = langId;
		this.icon = icon;
		this.questLogic = questLogic;
		this.taskLogic = taskLogic;
		VintageQuesting.QUESTS.register(id, this);
	}

	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public QuestTemplate setX(int x) {
		this.x = x;
		return this;
	}

	public QuestTemplate setX(QuestTemplate quest, int offset) {
		this.x = quest.getX() + offset;
		return this;
	}

	public int getY() {
		return y;
	}

	public QuestTemplate setY(int y) {
		this.y = y;
		return this;
	}

	public QuestTemplate setY(QuestTemplate quest, int offset) {
		this.y = quest.getY() + offset;
		return this;
	}

	public Achievement.Type getType() {
		return type;
	}

	public QuestTemplate setType(Achievement.Type type) {
		this.type = type;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getTranslatedName() {
		return Catalyst.translateNameKey(name);
	}

	public QuestTemplate setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public String getTranslatedDescription() {
		return Catalyst.translateDescKey(description);
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
}
