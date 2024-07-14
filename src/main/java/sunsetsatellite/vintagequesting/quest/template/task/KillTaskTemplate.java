package sunsetsatellite.vintagequesting.quest.template.task;

import net.minecraft.core.entity.Entity;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.KillTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class KillTaskTemplate extends TaskTemplate{

	protected Class<? extends Entity> requiredClass;
	protected int requiredCount;

	protected Task cache;

	public KillTaskTemplate(String id, Class<? extends Entity> required, int amount) {
		super(id,"type.task.vq.kill");
		this.requiredClass = required;
		this.requiredCount = amount;
	}

	@Override
	public TaskTemplate copy() {
		return new KillTaskTemplate(id,requiredClass,requiredCount);
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
	public void reset() {
		cache = null;
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

}
