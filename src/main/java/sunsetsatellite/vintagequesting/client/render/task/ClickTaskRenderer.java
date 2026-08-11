package sunsetsatellite.vintagequesting.client.render.task;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.gui.slot.task.GuiClickTaskSlot;
import sunsetsatellite.vintagequesting.client.render.TaskRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.task.ClickTask;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class ClickTaskRenderer extends TaskRenderer<ClickTask> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, ClickTask task, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i + 1) + ". " + task.data.getTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiClickTaskSlot(mc, width / 2 - 48, 20, true, task, quest, chapter));
	}
}
