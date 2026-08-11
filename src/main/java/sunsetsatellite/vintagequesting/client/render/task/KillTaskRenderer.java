package sunsetsatellite.vintagequesting.client.render.task;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.gui.slot.task.GuiKillTaskSlot;
import sunsetsatellite.vintagequesting.client.render.TaskRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.task.KillTask;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class KillTaskRenderer extends TaskRenderer<KillTask> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, KillTask task, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i + 1) + ". " + task.data.getTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiKillTaskSlot(mc, width / 2 - 48, 24, task));
	}
}
