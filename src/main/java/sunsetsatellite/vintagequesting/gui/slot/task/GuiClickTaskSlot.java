package sunsetsatellite.vintagequesting.gui.slot.task;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.FontRenderer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.interfaces.IClickable;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.task.ClickTask;

public class GuiClickTaskSlot extends Gui implements IRenderable, IClickable {

	private final int width;
	private final int height;
	public boolean visible;
	public boolean playSound;
	public String string;
	public Minecraft mc;
	public ClickTask task;

	public GuiClickTaskSlot(Minecraft mc, int width, int height, boolean visible, ClickTask task) {
		this.mc = mc;
		this.width = width;
		this.height = height;
		this.visible = visible;
		this.playSound = true;
		this.task = task;
	}

	protected int getButtonState(boolean hovered) {
		int state = 1;
		if (task.isCompleted()) {
			state = 0;
		} else if (hovered) {
			state = 2;
		}

		return state;
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		if (this.visible) {
			this.string = task.isCompleted() ? "✔" : "❌";
			FontRenderer fontrenderer = mc.fontRenderer;
			boolean mouseOver = mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
			int state = this.getButtonState(mouseOver);
			GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/minecraft/textures/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(x, y, 0, 46 + state * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(x + this.width / 2, y, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
			this.mouseDragged(mc, x,y, mouseX, mouseY);
			int textColor;
			switch (state) {
				case 0:
					textColor = 10526880;
					break;
				case 1:
					textColor = 14737632;
					break;
				default:
					textColor = 16777120;
			}

			this.drawStringCentered(fontrenderer, this.string, x + this.width / 2, y + (this.height - 8) / 2, textColor);
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
		return !this.task.isCompleted() && mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
	}

	@Override
	public void click() {
		task.click();
	}
}
