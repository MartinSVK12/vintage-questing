package sunsetsatellite.vintagequesting.core.data.task;

import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.core.instance.task.ClickTask;

public class ClickTaskData extends TaskData {

	protected Task cache;

	public ClickTaskData(String id) {
		super(id);
	}

	@Override
	public String getTypeId() {
		return "type.task.vq.click";
	}

	@Override
	public TaskData copy() {
		return new ClickTaskData(id);
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
	public void clearInstance() {
		cache = null;
	}

}
