package sunsetsatellite.vintagequesting.quest.template.task;

import net.minecraft.core.world.Dimension;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.VisitDimensionTask;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public class VisitDimensionTaskTemplate extends TaskTemplate {

	protected Task cache;
	protected Dimension dimension;

	public VisitDimensionTaskTemplate(String id, Dimension dimension) {
		super(id,"type.task.vq.visitDimension");
		this.dimension = dimension;
	}

	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public TaskTemplate copy() {
		return new VisitDimensionTaskTemplate(id,dimension);
	}

	@Override
	public Task getInstance() {
		return cache == null ? cache = getInstanceUnique() : cache;
	}

	@Override
	public Task getInstanceUnique() {
		return new VisitDimensionTask(this);
	}

	@Override
	public void reset() {
		cache = null;
	}

}
