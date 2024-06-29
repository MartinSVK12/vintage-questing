package sunsetsatellite.vintagequesting.quest.template.task;

import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.ClickTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class ClickTaskTemplate extends TaskTemplate {
	public ClickTaskTemplate(String id) {
		super(id,"type.task.vq.click");
	}

	@Override
	public TaskTemplate copy() {
		return new ClickTaskTemplate(id);
	}

	@Override
	public Task getInstance() {
		return new ClickTask(this);
	}

}
