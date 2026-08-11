package sunsetsatellite.vintagequesting.core.instance.task;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.TaskData;

public class ClickTask extends Task {

	private boolean clicked = false;

	public ClickTask(TaskData data) {
		super(data);
	}

	public void click() {
		clicked = true;
	}

	@Override
	public boolean isCompleted() {
		return clicked;
	}

	@Override
	public Task copy() {
		return new ClickTask(data);
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		clicked = nbt.getBoolean("Clicked");
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Clicked", clicked);
	}

	@Override
	public void forceComplete() {
		click();
	}

	@Override
	public void reset() {
		clicked = false;
	}

}
