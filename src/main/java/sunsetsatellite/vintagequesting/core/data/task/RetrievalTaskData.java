package sunsetsatellite.vintagequesting.core.data.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.core.instance.task.RetrievalTask;

public class RetrievalTaskData extends TaskData {

	protected Task cache;
	protected ItemStack requirement;
	protected boolean canConsume;
	protected boolean checkNbt;
	protected boolean ignoreMeta;

	public RetrievalTaskData(String id, ItemStack stack) {
		super(id);
		this.requirement = stack;
	}

	@Override
	public TaskData copy() {
		return new RetrievalTaskData(id, requirement);
	}

	public ItemStack getStack() {
		return requirement;
	}

	@Override
	public Task getInstanceUnique() {
		return new RetrievalTask(this);
	}

	@Override
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.task.vq.retrieval";
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	public RetrievalTaskData setConsume() {
		this.canConsume = true;
		return this;
	}

	public RetrievalTaskData setChecksNbt() {
		this.checkNbt = true;
		return this;
	}

	public RetrievalTaskData setIgnoreMeta() {
		this.ignoreMeta = true;
		return this;
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {
		return checkNbt;
	}

	public boolean ignoresMeta() {
		return ignoreMeta;
	}
}
