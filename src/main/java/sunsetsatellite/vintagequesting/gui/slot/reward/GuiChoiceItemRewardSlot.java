package sunsetsatellite.vintagequesting.gui.slot.reward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.interfaces.IClickable;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.reward.ChoiceItemReward;

public class GuiChoiceItemRewardSlot extends Gui implements IRenderable, IClickable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final ChoiceItemReward reward;
	private final GuiTooltip tooltip;
	private final int option;

	public GuiChoiceItemRewardSlot(Minecraft mc, int width, int height, ChoiceItemReward reward, int option){
		this.width = width;
        this.height = height;
		this.mc = mc;
		this.reward = reward;
		this.tooltip = new GuiTooltip(mc);
		this.option = option;
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if(reward.isRedeemed() && reward.getChosenStack() != null && reward.getChosenStack().isStackEqual(reward.getOption(option))){
			drawRectWidthHeight(x,y,width,height,0xFF008000);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		} else if (!reward.isRedeemed() && reward.getChosenStack() != null && reward.getChosenStack().isStackEqual(reward.getOption(option))) {
			drawRectWidthHeight(x,y,width,height,0xFF808080);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		drawSlot(mc.renderEngine, x+3,y+3,0xFFFFFFFF);
		GL11.glEnable(3553);
		ItemStack item = reward.getOption(option);
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
		reward.choose(option);
	}
}
