package sunsetsatellite.vintagequesting.quest.task;

import sunsetsatellite.vintagequesting.quest.Task;

public class ClickTask extends Task {

	private boolean clicked = false;

	public ClickTask() {
		super("task.vq.click");
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
		return new ClickTask();
	}
}
