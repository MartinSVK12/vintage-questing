package sunsetsatellite.vintagequesting.client.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import sunsetsatellite.vintagequesting.client.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.core.instance.task.VisitDimensionTask;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

public class GuiVisitDimensionTaskSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final VisitDimensionTask task;
	private final TooltipElement tooltip;

	public GuiVisitDimensionTaskSlot(Minecraft mc, int width, int height, VisitDimensionTask task) {
		this.width = width;
		this.height = height;
		this.mc = mc;
		this.task = task;
		this.tooltip = new TooltipElement(mc);
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if (task.isCompleted()) {
			drawRectWidthHeight(x, y, width, height, 0xFF008000);
		}
		ItemStack item = Blocks.PORTAL_NETHER.getDefaultStack();
		ItemRenderHelper.renderItemStack(item, x + 4, y + 2, 1, 1);
		GLRenderer.disableState(State.CULL_FACE);
		Lighting.disable();

		drawStringNoShadow(mc.font, "Visit " + task.getDimension().getTranslatedName(), x + 28, y + 6, 0xFFFFFFFF);

		if (mouseX > x + 3 && mouseX < x + 21 && mouseY > y + 3 && mouseY < y + 21) {

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
