package sunsetsatellite.vintagequesting.client.render.reward;

import net.minecraft.client.Minecraft;
import sunsetsatellite.vintagequesting.client.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.client.gui.slot.reward.GuiItemRewardSlot;
import sunsetsatellite.vintagequesting.client.render.RewardRenderer;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.reward.ItemReward;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.List;

public class ItemRewardRenderer extends RewardRenderer<ItemReward> {
	@Override
	public void renderSlot(Chapter chapter, Quest quest, ItemReward reward, Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, "Item Reward:", 0xFFFFFFFF));
		renderables.add(new GuiItemRewardSlot(mc, width / 2 - 38, 24, reward));
	}
}
