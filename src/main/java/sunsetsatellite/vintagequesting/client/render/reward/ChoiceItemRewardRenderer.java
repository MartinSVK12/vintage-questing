package sunsetsatellite.vintagequesting.client.render.reward;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.gui.slot.reward.GuiChoiceItemRewardSlot;
import sunsetsatellite.vintagequesting.client.render.RewardRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.reward.ChoiceItemReward;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class ChoiceItemRewardRenderer extends RewardRenderer<ChoiceItemReward> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, ChoiceItemReward reward, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, "Choice Item Reward:", 0xFFFFFFFF));
		for (int j = 0; j < reward.getStacks().size(); j++) {
			renderables.add(new GuiChoiceItemRewardSlot(mc,width / 2 - 38,24, reward, j));
		}
	}
}
