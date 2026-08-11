package sunsetsatellite.vintagequesting.core;

import com.mojang.nbt.tags.CompoundTag;
import sunsetsatellite.vintagequesting.core.data.TaskData;

public abstract class Task {

	public final TaskData data;

	public Task(TaskData data) {
		this.data = data;
	}

	public Task(TaskData data, CompoundTag tag) {
		this.data = data;
		readFromNbt(tag);
	}

	public abstract Task copy();

	public abstract void readFromNbt(CompoundTag nbt);

	public abstract void writeToNbt(CompoundTag nbt);

	public abstract boolean isCompleted();

	public abstract void forceComplete();

	public abstract void reset();
}
