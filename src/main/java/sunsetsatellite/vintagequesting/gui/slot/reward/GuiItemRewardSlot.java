package sunsetsatellite.vintagequesting.gui.slot.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;

public class GuiItemRewardSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private Minecraft mc;
	private ItemReward reward;
	private GuiTooltip tooltip;

	public GuiItemRewardSlot(Minecraft mc, int width, int height, ItemReward reward){
		this.width = width;
        this.height = height;
		this.mc = mc;
		this.reward = reward;
		this.tooltip = new GuiTooltip(mc);
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if(reward.isRedeemed()){
			drawRectWidthHeight(x,y,width,height,0xFF008000);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		drawSlot(mc.renderEngine, x+3,y+3,0xFFFFFFFF);
		GL11.glEnable(3553);
		ItemStack item = reward.getStack();
		ItemRenderHelper.renderItemStack(item, x + 4, y + 4, 1, 1, 1, 1);
		GL11.glDisable(2896);
		GL11.glDisable(2884);
		Lighting.disable();

		drawString(mc.fontRenderer, item.stackSize+"x "+ item.getDisplayName(),x+28, y+8, 0xFFFFFFFF);

		if(mouseX > x+3 && mouseX < x+21 && mouseY > y+3 && mouseY < y+21){

			boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
			String tooltipText = tooltip.getTooltipText(item, ctrl);
			tooltip.render(tooltipText,mouseX,mouseY,8,-8);
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
}
