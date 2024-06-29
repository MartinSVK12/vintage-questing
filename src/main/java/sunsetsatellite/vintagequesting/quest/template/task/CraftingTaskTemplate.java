package sunsetsatellite.vintagequesting.quest.template.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.CraftingTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class CraftingTaskTemplate extends TaskTemplate {

	protected ItemStack requirement;
	protected boolean canConsume;
	protected boolean checkNbt;

	public CraftingTaskTemplate(String id, ItemStack stack) {
		super(id,"type.task.vq.crafting");
		this.requirement = stack;
	}

	@Override
	public TaskTemplate copy() {
		return new CraftingTaskTemplate(id,requirement);
	}

	public ItemStack getStack(){
		return requirement;
	}

	@Override
	public Task getInstance() {
		return new CraftingTask(this);
	}

	public CraftingTaskTemplate setConsume() {
		this.canConsume = true;
		return this;
	}

	public CraftingTaskTemplate setCheckNbt() {
		this.checkNbt = true;
		return this;
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checkNbt;}
}
