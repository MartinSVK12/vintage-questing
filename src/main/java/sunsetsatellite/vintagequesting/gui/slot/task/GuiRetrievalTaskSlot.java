package sunsetsatellite.vintagequesting.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;

public class GuiRetrievalTaskSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private Minecraft mc;
	private RetrievalTask task;
	private GuiTooltip tooltip;

	public GuiRetrievalTaskSlot(Minecraft mc, int width, int height, RetrievalTask task){
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
		drawSlot(mc.renderEngine, x+3,y+3,0xFFFFFFFF);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		ItemStack item = task.getItem();
		ItemModel model = ItemModelDispatcher.getInstance().getDispatch(item);
		Lighting.enableInventoryLight();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
		model.renderItemIntoGui(Tessellator.instance, mc.fontRenderer, mc.renderEngine, item, x+4,y+4, 1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		Lighting.disable();

		drawString(mc.fontRenderer, task.getProgress()+" / "+item.stackSize+"x "+ item.getDisplayName(),x+28, y+8, 0xFFFFFFFF);

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
