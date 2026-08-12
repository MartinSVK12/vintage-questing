package sunsetsatellite.vintagequesting.client.gui.slot.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;


import sunsetsatellite.vintagequesting.client.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.instance.reward.ChoiceItemReward;
import sunsetsatellite.vintagequesting.interfaces.IClickable;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageChooseItemReward;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class GuiChoiceItemRewardSlot extends Gui implements IRenderable, IClickable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final ChoiceItemReward reward;
	private final TooltipElement tooltip;
	private final int option;
	public Chapter chapter;
	public Quest quest;

	public GuiChoiceItemRewardSlot(Minecraft mc, int width, int height, ChoiceItemReward reward, int option, Chapter chapter, Quest quest) {
		this.width = width;
		this.height = height;
		this.mc = mc;
		this.reward = reward;
		this.tooltip = new TooltipElement(mc);
		this.option = option;
		this.chapter = chapter;
		this.quest = quest;
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if (reward.isRedeemed() && reward.getChosenStack() != null && reward.getChosenStack().isStackEqual(reward.getOption(option))) {
			drawRectWidthHeight(x, y, width, height, 0xFF008000);
		} else if (!reward.isRedeemed() && reward.getChosenStack() != null && reward.getChosenStack().isStackEqual(reward.getOption(option))) {
			drawRectWidthHeight(x, y, width, height, 0xFF808080);
		}
		drawSlot(mc.textureManager, x + 3, y + 3, 0xFFFFFFFF);
		ItemStack item = reward.getOption(option);
		ItemRenderHelper.renderItemStack(item, x + 4, y + 4, 1, 1);
		Lighting.disable();
		GLRenderer.disableState(State.CULL_FACE);

		drawStringNoShadow(mc.font, item.stackSize + "x " + item.getDisplayName(), x + 28, y + 8, 0xFFFFFFFF);

		if (mouseX > x + 3 && mouseX < x + 21 && mouseY > y + 3 && mouseY < y + 21) {

			boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
			String tooltipText = tooltip.getTooltipText(item, ctrl);
			tooltip.render(tooltipText, mouseX, mouseY, 8, -8);
		}
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public boolean isHovered(int x, int y, int mouseX, int mouseY) {
		return mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
	}

	@Override
	public void mouseDragged(Minecraft mc, int x, int y, int mouseX, int mouseY) {

	}

	@Override
	public void mouseReleased(int x, int y, int mouseX, int mouseY) {

	}

	@Override
	public boolean mouseClicked(Minecraft mc, int x, int y, int mouseX, int mouseY) {
		return !this.reward.isRedeemed() && mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
	}

	@Override
	public void click() {
		if(EnvironmentHelper.isMultiplayerClient()){
			NetworkHandler.sendToServer(new NetworkMessageChooseItemReward(chapter.getId(), quest.data.getId(), reward.data.getId(), option));
		}
		reward.choose(option);
	}

	public void drawSlot(TextureManager re, int x, int y, int argb) {
		float a = (float) (argb >> 24 & 255) / 255.0F;
		float r = (float) (argb >> 16 & 255) / 255.0F;
		float g = (float) (argb >> 8 & 255) / 255.0F;
		float b = (float) (argb & 255) / 255.0F;
		re.bindTexture(re.loadTexture("/assets/vintagequesting/textures/gui/slot.png"));
		GLRenderer.setColor4f(r, g, b, a);
		this.drawTexturedModalRect(x, y, 0, 0, 18, 18, 18, 0.0078125F);
	}
}
