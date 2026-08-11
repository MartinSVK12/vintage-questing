package sunsetsatellite.vintagequesting.core.data.task;

import net.minecraft.core.entity.Entity;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.core.instance.task.KillTask;

public class KillTaskData extends TaskData {

	protected Class<? extends Entity> requiredClass;
	protected int requiredCount;

	protected Task cache;

	public KillTaskData(String id, Class<? extends Entity> required, int amount) {
		super(id);
		this.requiredClass = required;
		this.requiredCount = amount;
	}

	@Override
	public TaskData copy() {
		return new KillTaskData(id, requiredClass, requiredCount);
	}

	public Class<? extends Entity> getEntityClass() {
		return requiredClass;
	}

	public int getRequiredCount() {
		return requiredCount;
	}

	@Override
	public Task getInstanceUnique() {
		return new KillTask(this);
	}

	@Override
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.task.vq.kill";
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

}
