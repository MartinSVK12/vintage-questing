package sunsetsatellite.vintagequesting.quest.template.task;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class RetrievalTaskTemplate extends TaskTemplate {

	protected ItemStack requirement;

	public RetrievalTaskTemplate(ItemStack stack) {
		super("type.task.vq.retrieval");
		this.requirement = stack;
	}

	@Override
	public TaskTemplate copy() {
		return new RetrievalTaskTemplate(requirement);
	}

	public ItemStack getStack(){
		return requirement;
	}

	@Override
	public Task getInstance() {
		return new RetrievalTask(this);
	}
}
