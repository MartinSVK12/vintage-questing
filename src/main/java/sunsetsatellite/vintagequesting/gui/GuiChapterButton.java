package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.quest.Chapter;

import java.awt.*;

public class GuiChapterButton extends GuiButton {

	protected final Chapter chapter;

	public GuiChapterButton(int id, Chapter chapter, int xPosition, int yPosition, int width, int height) {
		super(id, xPosition, yPosition, width, height, chapter.getTranslatedName());
		this.chapter = chapter;
	}

	public Chapter getChapter() {
		return chapter;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			boolean mouseOver = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int state = this.getButtonState(mouseOver);
			GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/minecraft/textures/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + state * 20, this.width / 2, this.height);
			this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
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

			ItemModel model = ItemModelDispatcher.getInstance().getDispatch(chapter.getIcon().getDefaultStack());

			model.renderItemIntoGui(Tessellator.instance, fontrenderer, mc.renderEngine, chapter.getIcon().getDefaultStack(),this.xPosition - 16, this.yPosition, 1);

			GL11.glDisable(2896);
			GL11.glDisable(2884);

			this.drawString(fontrenderer, this.displayString, this.xPosition + 6, this.yPosition + (this.height - 8) / 2, textColor);
		}
	}

}
