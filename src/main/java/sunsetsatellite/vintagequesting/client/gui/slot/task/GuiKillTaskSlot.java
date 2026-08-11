package sunsetsatellite.vintagequesting.client.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.entity.EntityDispatcher;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import org.lwjgl.input.Keyboard;
import sunsetsatellite.vintagequesting.client.gui.ItemRenderHelper;
import sunsetsatellite.vintagequesting.core.instance.task.KillTask;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

public class GuiKillTaskSlot extends Gui implements IRenderable {

	public int width;
	public int height;
	private final Minecraft mc;
	private final KillTask task;
	private final TooltipElement tooltip;

	public GuiKillTaskSlot(Minecraft mc, int width, int height, KillTask task) {
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
		ItemStack item = Items.TOOL_SWORD_IRON.getDefaultStack();
		ItemRenderHelper.renderItemStack(item, x + 4, y + 4, 1, 1);
		GLRenderer.disableState(State.CULL_FACE);
		Lighting.disable();

		drawStringNoShadow(mc.font, task.getProgress() + " / " + task.getRequiredCount() + "x " + EntityDispatcher.getInstance().classToEntryMap.get(task.getEntityClass()).nameKey + " (" + task.getEntityClass().getSimpleName() + ")", x + 28, y + 8, 0xFFFFFFFF);

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
