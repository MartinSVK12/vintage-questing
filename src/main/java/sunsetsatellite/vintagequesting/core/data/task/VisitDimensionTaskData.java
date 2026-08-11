package sunsetsatellite.vintagequesting.core.data.task;

import net.minecraft.core.world.Dimension;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.core.instance.task.VisitDimensionTask;

public class VisitDimensionTaskData extends TaskData {

	protected Task cache;
	protected Dimension dimension;

	public VisitDimensionTaskData(String id, Dimension dimension) {
		super(id);
		this.dimension = dimension;
	}

	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public TaskData copy() {
		return new VisitDimensionTaskData(id, dimension);
	}

	@Override
	public String getTypeId() {
		return "type.task.vq.visitDimension";
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
	public void clearInstance() {
		cache = null;
	}

}
