package sunsetsatellite.vintagequesting.client.gui.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

public class StringElement extends Gui implements IRenderable {

	private final Minecraft mc;
	public String string;
	public int argb;

	public StringElement(Minecraft mc, String string, int argb) {
		this.string = string;
		this.mc = mc;
		this.argb = argb;
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		mc.font.render(string, x, y).setColor(argb).call();
	}

	@Override
	public int getHeight() {
		return mc.font.getFont().fontHeight();
	}

	@Override
	public int getWidth() {
		return mc.font.stringWidth(string);
	}
}
