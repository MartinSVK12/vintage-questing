package sunsetsatellite.vintagequesting.client.render.task;

import net.minecraft.client.Minecraft;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.gui.slot.task.GuiRetrievalTaskSlot;
import sunsetsatellite.vintagequesting.client.render.TaskRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.task.RetrievalTask;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class RetrievalTaskRenderer extends TaskRenderer<RetrievalTask> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, RetrievalTask task, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i + 1) + ". " + task.data.getTypeName() + " | Consume: " + (task.canConsume() ? TextFormatting.RED : TextFormatting.WHITE) + task.canConsume(), 0xFFFFFFFF));
		renderables.add(new GuiRetrievalTaskSlot(mc, width / 2 - 48, 24, task));
	}
}
