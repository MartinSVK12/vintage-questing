package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.FontRenderer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.quest.Quest;

public class GuiQuestButton extends GuiButton {

	protected final Quest quest;
	protected final GuiQuestbook guiQuestbook;
	protected int iconSize;

	public GuiQuestButton(GuiQuestbook guiQuestbook, int id, Quest quest) {
		super(id, quest.getX(), quest.getY(), quest.getWidth(), quest.getHeight(),"");
		this.quest = quest;
		this.guiQuestbook = guiQuestbook;
		this.iconSize = quest.getIconSize()+1;
	}

	public Quest getQuest() {
		return quest;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;

			int x = xPosition+guiQuestbook.currentX;
			int y = yPosition+guiQuestbook.currentY;


			if(quest.preRequisitesCompleted() && quest.isCompleted() && quest.areAllRewardsRedeemed()){
				drawRectWidthHeight(x-4,y-4, width+8,height+8, 0xFF00AA00);
				drawRectWidthHeight(x-2,y-2, width+4,height+4, 0xFF006000);
			} else if(quest.preRequisitesCompleted() && quest.isCompleted() && !quest.areAllRewardsRedeemed()){
				drawRectWidthHeight(x-4,y-4, width+8,height+8, 0xFF00AAAA);
				drawRectWidthHeight(x-2,y-2, width+4,height+4, 0xFF006060);
			} else if(quest.preRequisitesCompleted() && !quest.isCompleted()) {
				drawRectWidthHeight(x-4,y-4, width+8,height+8, 0xFFAA0000);
				drawRectWidthHeight(x-2,y-2, width+4,height+4, 0xFF600000);
			} else {
				drawRectWidthHeight(x-4,y-4, width+8,height+8, 0xFF606060);
				drawRectWidthHeight(x-2,y-2, width+4,height+4, 0xFF303030);
			}

			GL11.glEnable(3553);

			GL11.glPushMatrix();
			if(iconSize >= 3){
				GL11.glTranslatef(3 * iconSize,3 * iconSize,0);
				ItemRenderHelper.renderItemStack(quest.getIcon().getDefaultStack(),x, y, iconSize, iconSize, 1, 1);
			} else {
				ItemRenderHelper.renderItemStack(quest.getIcon().getDefaultStack(),x, y, iconSize, iconSize, 1, 1);
			}

			GL11.glPopMatrix();
			GL11.glDisable(2896);
			GL11.glDisable(2884);
		}
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		int x = (int) ((xPosition * guiQuestbook.zoom)+(guiQuestbook.currentX * guiQuestbook.zoom));
		int y = (int) ((yPosition * guiQuestbook.zoom)+(guiQuestbook.currentY * guiQuestbook.zoom));
		return mouseX >= x && mouseY >= y && mouseX < x + (this.width * guiQuestbook.zoom) && mouseY < y + (this.height * guiQuestbook.zoom);
	}

	@Override
	public boolean mouseClicked(Minecraft mc, int mouseX, int mouseY) {
		int x = (int) ((xPosition * guiQuestbook.zoom)+(guiQuestbook.currentX * guiQuestbook.zoom));
		int y = (int) ((yPosition * guiQuestbook.zoom)+(guiQuestbook.currentY * guiQuestbook.zoom));
		return this.enabled && mouseX >= x && mouseY >= y && mouseX < x +(this.width * guiQuestbook.zoom) && mouseY < y + (this.height * guiQuestbook.zoom);
	}
}
