package sunsetsatellite.vintagequesting.core.data;

import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Task;

public abstract class TaskData {

	protected final String id;

	public TaskData(String id) {
		this.id = id;
		VintageQuesting.TASKS.register(id, this);
	}

	public String getId() {
		return id;
	}

	public abstract String getTypeId();

	public String getTypeName() {
		return Catalyst.translateNameKey(getTypeId());
	}

	public abstract Task getInstance();

	public abstract Task getInstanceUnique();

	public abstract void clearInstance();

	public abstract TaskData copy();
}
