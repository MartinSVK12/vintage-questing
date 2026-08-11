package sunsetsatellite.vintagequesting.core.data.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.core.instance.task.CraftingTask;


public class CraftingTaskData extends TaskData {

	protected ItemStack requirement;
	protected boolean canConsume;
	protected boolean checkNbt;
	protected boolean ignoreMeta;
	protected Task cache;

	public CraftingTaskData(String id, ItemStack stack) {
		super(id);
		this.requirement = stack;
	}

	@Override
	public TaskData copy() {
		return new CraftingTaskData(id, requirement);
	}

	public ItemStack getStack() {
		return requirement;
	}

	@Override
	public Task getInstanceUnique() {
		return new CraftingTask(this);
	}

	@Override
	public void clearInstance() {
		cache = null;
	}

	@Override
	public String getTypeId() {
		return "type.task.vq.crafting";
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	public CraftingTaskData setConsume() {
		this.canConsume = true;
		return this;
	}

	public CraftingTaskData setCheckNbt() {
		this.checkNbt = true;
		return this;
	}

	public CraftingTaskData setIgnoreMeta() {
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
