package sunsetsatellite.vintagequesting.gui.slot.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;


import sunsetsatellite.vintagequesting.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;

public class GuiItemRewardSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final ItemReward reward;
	private final TooltipElement tooltip;

	public GuiItemRewardSlot(Minecraft mc, int width, int height, ItemReward reward) {
		this.width = width;
		this.height = height;
		this.mc = mc;
		this.reward = reward;
		this.tooltip = new TooltipElement(mc);
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if (reward.isRedeemed()) {
			drawRectWidthHeight(x, y, width, height, 0xFF008000);
		}
		drawSlot(mc.textureManager, x + 3, y + 3, 0xFFFFFFFF);
		ItemStack item = reward.getStack();
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
