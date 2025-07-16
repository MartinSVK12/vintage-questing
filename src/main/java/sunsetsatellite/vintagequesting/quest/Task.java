package sunsetsatellite.vintagequesting.quest;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

import java.util.List;

public abstract class Task {

	protected String typeName;
	protected final TaskTemplate template;

	public Task(TaskTemplate template) {
		this.template = template;
		this.typeName = template.getTypeName();
	}

	public Task(TaskTemplate template, CompoundTag tag) {
		this.template = template;
		this.typeName = template.getTypeName();
		readFromNbt(tag);
	}


	public abstract boolean isCompleted();

	public String getTypeName() {
		return typeName;
	}

	public String getTranslatedTypeName(){
		return I18n.getInstance().translateNameKey(typeName);
	}

	public abstract Task copy();

	public abstract void readFromNbt(CompoundTag nbt);

	public abstract void writeToNbt(CompoundTag nbt);

	public TaskTemplate getTemplate() {
		return template;
	}

	public abstract void renderSlot(Minecraft mc, List<IRenderable> renderables, int i, int width);

	public abstract void forceComplete();

	public abstract void reset();
}
