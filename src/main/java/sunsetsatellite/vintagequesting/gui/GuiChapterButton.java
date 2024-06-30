package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.FontRenderer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Chapter;

public class GuiChapterButton extends GuiButton implements IRenderable {

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
		enabled = ((IHasQuests) Minecraft.getMinecraft(this).thePlayer).getCurrentChapter() != chapter;
		if (this.visible) {
			if(((IHasQuests) mc.thePlayer).getQuestGroup().chapters.stream().allMatch(Chapter::areAllQuestsCompleted)){
				drawRectWidthHeight(xPosition,yPosition,width,height,0xFFD6B400);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			else if(chapter.areAllQuestsCompleted()){
				drawRectWidthHeight(xPosition,yPosition,width,height,0xFF008000);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			FontRenderer fontrenderer = mc.fontRenderer;
			int offset = 20;
			boolean mouseOver = mouseX >= (this.xPosition + offset) && mouseY >= this.yPosition && mouseX < (this.xPosition + offset) + this.width && mouseY < this.yPosition + this.height;
			int state = this.getButtonState(mouseOver);
			GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/minecraft/textures/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect((this.xPosition + offset), this.yPosition, 0, 46 + state * 20, this.width / 2, this.height);
			this.drawTexturedModalRect((this.xPosition + offset) + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + state * 20, this.width / 2, this.height);
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

			ItemRenderHelper.renderItemStack(chapter.getIcon().getDefaultStack(),this.xPosition + 2, this.yPosition + 2, 1, 1, 1, 1);

			GL11.glDisable(2896);
			GL11.glDisable(2884);

			this.drawString(fontrenderer, this.displayString, (this.xPosition + offset) + 6, this.yPosition + (this.height - 8) / 2, textColor);
		}
	}

	@Override
	public void render(int x, int y, int mouseX, int mouseY) {
		xPosition = x;
		yPosition = y;
		drawButton(Minecraft.getMinecraft(this), mouseX, mouseY);
	}
}
