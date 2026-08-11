package sunsetsatellite.vintagequesting.core.instance.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.task.CraftingTaskData;

public class CraftingTask extends Task {

	protected ItemStack requirement;
	protected int progress = 0;
	protected boolean canConsume;
	protected boolean checksNbt;
	protected boolean ignoreMeta;

	public CraftingTask(CraftingTaskData data) {
		super(data);
		this.requirement = data.getStack();
		this.canConsume = data.canConsume();
		this.checksNbt = data.checksNbt();
		this.ignoreMeta = data.ignoresMeta();
	}

	public int addProgress(ItemStack stack) {
		if (stack == null) return -1;
		if (stack.isItemEqual(requirement) || (ignoreMeta && stack.itemID == requirement.itemID)) {
			if (checksNbt && !(stack.getData().equals(requirement.getData()))) {
				return -1;
			}
			progress += stack.stackSize;
			return stack.stackSize;
		}
		return -1;
	}

	public void resetProgress() {
		progress = 0;
	}

	public ItemStack getStack() {
		return requirement;
	}

	public int getProgress() {
		return progress;
	}

	@Override
	public boolean isCompleted() {
		return progress >= requirement.stackSize;
	}

	@Override
	public Task copy() {
		return new CraftingTask((CraftingTaskData) data);
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		progress = nbt.getInteger("Progress");
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		nbt.putInt("Progress", progress);
	}

	@Override
	public void forceComplete() {
		progress = requirement.stackSize;
	}

	@Override
	public void reset() {
		progress = 0;
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {
		return checksNbt;
	}

	public boolean ignoresMeta() {
		return ignoreMeta;
	}
}
