package sunsetsatellite.vintagequesting.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.task.CraftingTask;
import sunsetsatellite.vintagequesting.quest.task.KillTask;

public class GuiKillTaskSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final KillTask task;
	private final GuiTooltip tooltip;

	public GuiKillTaskSlot(Minecraft mc, int width, int height, KillTask task){
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
		ItemStack item = Item.toolSwordIron.getDefaultStack();
		ItemRenderHelper.renderItemStack(item, x + 4, y + 4, 1, 1, 1,1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		Lighting.disable();

		drawString(mc.fontRenderer, task.getProgress()+" / "+task.getRequiredCount()+"x "+ EntityDispatcher.classToKeyMap.get(task.getEntityClass()) + " ("+task.getEntityClass().getSimpleName()+")",x+28, y+8, 0xFFFFFFFF);

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
