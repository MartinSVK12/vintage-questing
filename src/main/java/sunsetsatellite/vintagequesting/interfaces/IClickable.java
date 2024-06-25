package sunsetsatellite.vintagequesting.interfaces;

import net.minecraft.client.Minecraft;

public interface IClickable {
	boolean isHovered(int x, int y, int mouseX, int mouseY);

	void mouseDragged(Minecraft mc, int x, int y, int mouseX, int mouseY);

	void mouseReleased(int x, int y, int mouseX, int mouseY);

	boolean mouseClicked(Minecraft mc, int x, int y, int mouseX, int mouseY);

	void click();
}
