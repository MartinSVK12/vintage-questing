package sunsetsatellite.vintagequesting.core.instance.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.task.VisitDimensionTaskData;

public class VisitDimensionTask extends Task {

	protected boolean visited = false;
	protected Dimension dimension;

	public VisitDimensionTask(VisitDimensionTaskData data) {
		super(data);
		dimension = data.getDimension();
	}

	public void check(Player player) {
		if (!visited) visited = player.dimension == dimension.id;
	}

	@Override
	public boolean isCompleted() {
		return visited;
	}

	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public Task copy() {
		return new VisitDimensionTask((VisitDimensionTaskData) data);
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		visited = nbt.getBoolean("Visited");
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Visited", visited);
	}

	@Override
	public void forceComplete() {
		visited = true;
	}

	@Override
	public void reset() {
		visited = false;
	}

}
