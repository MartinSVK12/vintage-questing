package sunsetsatellite.vintagequesting.quest.task;

import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class ClickTask extends Task {

	private boolean clicked = false;

	public ClickTask(TaskTemplate template) {
		super(template);
	}

	public void click() {
        clicked = true;
    }

	@Override
	public boolean isCompleted() {
		return clicked;
	}

	@Override
	public Task copy() {
		return new ClickTask(template);
	}
}
