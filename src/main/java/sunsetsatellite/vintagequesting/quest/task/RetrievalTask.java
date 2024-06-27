package sunsetsatellite.vintagequesting.quest.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.task.RetrievalTaskTemplate;

public class RetrievalTask extends Task {

	protected ItemStack requirement;
	protected int progress = 0;

	public RetrievalTask(RetrievalTaskTemplate template) {
		super(template);
		this.requirement = template.getStack();
	}

	public int addProgress(ItemStack stack) {
		if(stack == null) return -1;
		if(stack.isItemEqual(requirement)){
			if(checkNbt && !(stack.getData().equals(requirement.getData()))){
				return -1;
			}
			progress += stack.stackSize;
			return stack.stackSize;
		}
		return -1;
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
		return new RetrievalTask((RetrievalTaskTemplate) template);
	}
}
