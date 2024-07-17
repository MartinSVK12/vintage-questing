package sunsetsatellite.vintagequesting.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.task.VisitDimensionTask;

public class GuiVisitDImensionTaskSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final VisitDimensionTask task;
	private final GuiTooltip tooltip;

	public GuiVisitDImensionTaskSlot(Minecraft mc, int width, int height, VisitDimensionTask task){
		this.width = width;
        this.height = height;
		this.mc = mc;
		this.task = task;
		this.tooltip = new GuiTooltip(mc);
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if(task.isCompleted()){
			drawRectWidthHeight(x,y,width,height,0xFF008000);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		ItemStack item = Block.portalNether.getDefaultStack();
		ItemRenderHelper.renderItemStack(item, x + 4, y+2, 1, 1, 1,1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		Lighting.disable();

		drawString(mc.fontRenderer, "Visit "+task.getDimension().getTranslatedName(),x+28, y+6, 0xFFFFFFFF);

		if(mouseX > x+3 && mouseX < x+21 && mouseY > y+3 && mouseY < y+21){

			boolean ctrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
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
