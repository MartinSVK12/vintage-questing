package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiClickTaskSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

import java.util.List;

public class ClickTask extends Task {

    private boolean clicked = false;

	public ClickTask(TaskTemplate template) {
		super(template);
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
		return new ClickTask(template);
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
	public void renderSlot(Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i+1)+". "+this.getTranslatedTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiClickTaskSlot(mc, width / 2 - 48, 20, true, this));
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
