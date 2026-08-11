package sunsetsatellite.vintagequesting.core.instance.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.task.KillTaskData;

public class KillTask extends Task {

	protected Class<? extends Entity> requiredClass;
	protected int requiredCount;
	protected int progress = 0;

	public KillTask(KillTaskData data) {
		super(data);
		this.requiredClass = data.getEntityClass();
		this.requiredClass = data.getEntityClass();
		this.requiredCount = data.getRequiredCount();
	}

	public void addProgress(Class<? extends Entity> entity) {
		if (entity == requiredClass) {
			progress++;
		}
	}

	public void resetProgress() {
		progress = 0;
	}

	public Class<? extends Entity> getEntityClass() {
		return requiredClass;
	}

	public int getProgress() {
		return progress;
	}

	@Override
	public boolean isCompleted() {
		return progress >= requiredCount;
	}

	public int getRequiredCount() {
		return requiredCount;
	}

	@Override
	public Task copy() {
		return new KillTask((KillTaskData) data);
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
		progress = requiredCount;
	}

	@Override
	public void reset() {
		progress = 0;
	}
}
