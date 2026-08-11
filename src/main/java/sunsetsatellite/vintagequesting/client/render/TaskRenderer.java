package sunsetsatellite.vintagequesting.client.render;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public abstract class TaskRenderer<T extends Task> {

	public abstract void renderSlot(Chapter chapter, Quest quest, T task, Minecraft mc, List<IRenderable> renderables, int i, int width);

}
