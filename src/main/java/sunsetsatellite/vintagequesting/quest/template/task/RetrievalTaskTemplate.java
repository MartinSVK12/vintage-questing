package sunsetsatellite.vintagequesting.quest.template.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class RetrievalTaskTemplate extends TaskTemplate {

	protected Task cache;
	protected ItemStack requirement;
	protected boolean canConsume;
	protected boolean checkNbt;

	public RetrievalTaskTemplate(String id, ItemStack stack) {
		super(id,"type.task.vq.retrieval");
		this.requirement = stack;
	}

	@Override
	public TaskTemplate copy() {
		return new RetrievalTaskTemplate(id,requirement);
	}

	public ItemStack getStack(){
		return requirement;
	}

	@Override
	public Task getInstanceUnique() {
		return new RetrievalTask(this);
	}

	@Override
	public void reset() {
		cache = null;
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	public RetrievalTaskTemplate setConsume() {
		this.canConsume = true;
		return this;
	}

	public RetrievalTaskTemplate setChecksNbt() {
		this.checkNbt = true;
		return this;
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checkNbt;}
}
