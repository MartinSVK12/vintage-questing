package sunsetsatellite.vintagequesting.gui.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

public class GuiString extends Gui implements IRenderable {

	private final Minecraft mc;
	public String string;
	public int argb;

	public GuiString(Minecraft mc, String string, int argb) {
		this.string = string;
		this.mc = mc;
		this.argb = argb;
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		drawString(mc.fontRenderer,string,x,y,argb);
	}

	@Override
	public int getHeight() {
		return mc.fontRenderer.fontHeight;
	}

	@Override
	public int getWidth() {
		return mc.fontRenderer.getStringWidth(string);
	}
}
