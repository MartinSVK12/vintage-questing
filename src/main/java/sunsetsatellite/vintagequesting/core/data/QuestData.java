package sunsetsatellite.vintagequesting.core.data;

import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.item.IItemConvertible;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.util.Logic;

import java.util.ArrayList;
import java.util.List;

public class QuestData {
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
	protected List<TaskData> tasks = new ArrayList<>();
	protected List<QuestData> preRequisites = new ArrayList<>();
	protected List<RewardData> rewards = new ArrayList<>();
	protected Achievement.Type type = Achievement.TYPE_NORMAL;

	public QuestData(String id, String name, String description, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.icon = icon;
		this.questLogic = questLogic;
		this.taskLogic = taskLogic;
		VintageQuesting.QUESTS.register(id, this);
	}

	public QuestData(String id, String langId, IItemConvertible icon, Logic questLogic, Logic taskLogic) {
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

	public QuestData setX(int x) {
		this.x = x;
		return this;
	}

	public QuestData setX(QuestData quest, int offset) {
		this.x = quest.getX() + offset;
		return this;
	}

	public int getY() {
		return y;
	}

	public QuestData setY(int y) {
		this.y = y;
		return this;
	}

	public QuestData setY(QuestData quest, int offset) {
		this.y = quest.getY() + offset;
		return this;
	}

	public Achievement.Type getType() {
		return type;
	}

	public QuestData setType(Achievement.Type type) {
		this.type = type;
		return this;
	}

	public String getName() {
		return name;
	}

	public String getTranslatedName() {
		return Catalyst.translateNameKey(name);
	}

	public QuestData setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public String getTranslatedDescription() {
		return Catalyst.translateDescKey(description);
	}

	public QuestData setDescription(String description) {
		this.description = description;
		return this;
	}

	public IItemConvertible getIcon() {
		return icon;
	}

	public QuestData setIcon(IItemConvertible icon) {
		this.icon = icon;
		return this;
	}

	public Logic getQuestLogic() {
		return questLogic;
	}

	public QuestData setQuestLogic(Logic questLogic) {
		this.questLogic = questLogic;
		return this;
	}

	public Logic getTaskLogic() {
		return taskLogic;
	}

	public QuestData setTaskLogic(Logic taskLogic) {
		this.taskLogic = taskLogic;
		return this;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public QuestData setRepeat(boolean repeat) {
		this.repeat = repeat;
		return this;
	}

	public int getRepeatTicks() {
		return repeatTicks;
	}

	public QuestData setRepeatTicks(int repeatTicks) {
		this.repeatTicks = repeatTicks;
		return this;
	}

	public List<TaskData> getTasks() {
		return tasks;
	}

	public QuestData setTasks(List<TaskData> tasks) {
		this.tasks = tasks;
		return this;
	}

	public List<QuestData> getPreRequisites() {
		return preRequisites;
	}

	public QuestData setPreRequisites(List<QuestData> preRequisites) {
		this.preRequisites = preRequisites;
		return this;
	}

	public List<RewardData> getRewards() {
		return rewards;
	}

	public QuestData setRewards(List<RewardData> rewards) {
		this.rewards = rewards;
		return this;
	}
}
