package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import sunsetsatellite.vintagequesting.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiKillTaskSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.task.KillTaskTemplate;

import java.util.List;

public class KillTask extends Task {

	protected Class<? extends Entity> requiredClass;
	protected int requiredCount;
	protected int progress = 0;

	public KillTask(KillTaskTemplate template) {
		super(template);
		this.requiredClass = template.getEntityClass();
		this.requiredClass = template.getEntityClass();
		this.requiredCount = template.getRequiredCount();
	}

	public void addProgress(Class<? extends Entity> entity) {
		if(entity == requiredClass){
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
		return new KillTask((KillTaskTemplate) template);
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
	public void renderSlot(Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i+1)+". "+this.getTranslatedTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiKillTaskSlot(mc, width / 2 - 48, 24, this));
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
