package sunsetsatellite.vintagequesting.quest.template.task;

import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.ClickTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class ClickTaskTemplate extends TaskTemplate {

	protected Task cache;

	public ClickTaskTemplate(String id) {
		super(id,"type.task.vq.click");
	}

	@Override
	public TaskTemplate copy() {
		return new ClickTaskTemplate(id);
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	@Override
	public Task getInstanceUnique() {
		return new ClickTask(this);
	}

	@Override
	public void reset() {
		cache = null;
	}

}
