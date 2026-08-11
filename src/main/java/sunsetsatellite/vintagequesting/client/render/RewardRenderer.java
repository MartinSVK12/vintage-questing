package sunsetsatellite.vintagequesting.client.render;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public abstract class RewardRenderer<R extends Reward> {

	public abstract void renderSlot(Chapter chapter, Quest quest, R reward, Minecraft mc, List<IRenderable> renderables, int i, int width);

}
