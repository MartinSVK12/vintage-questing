package sunsetsatellite.vintagequesting.client.render.reward;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.render.RewardRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.reward.ScoreReward;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class ScoreRewardRenderer extends RewardRenderer<ScoreReward> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, ScoreReward reward, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, "Score Reward:", 0xFFFFFFFF));
		renderables.add(new StringElement(mc, "Score +" + reward.getAmount(), 0xFF00FF00));
	}
}
