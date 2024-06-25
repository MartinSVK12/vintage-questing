package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
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

			ItemModel model = ItemModelDispatcher.getInstance().getDispatch(quest.getIcon().getDefaultStack());

			GL11.glPushMatrix();
			GL11.glTranslatef(x,y,0);
			Lighting.enableInventoryLight();
			GL11.glScalef(iconSize,iconSize,0);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			BlockModel.setRenderBlocks(EntityRenderDispatcher.instance.itemRenderer.renderBlocksInstance);
			model.renderItemIntoGui(Tessellator.instance, fontrenderer, mc.renderEngine, quest.getIcon().getDefaultStack(), 0,0, 1);
			GL11.glPopMatrix();
			GL11.glDisable(2896);
			GL11.glDisable(2884);
		}
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		int x = xPosition+guiQuestbook.currentX;
		int y = yPosition+guiQuestbook.currentY;
		return mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
	}

	@Override
	public boolean mouseClicked(Minecraft mc, int mouseX, int mouseY) {
		int x = xPosition+guiQuestbook.currentX;
		int y = yPosition+guiQuestbook.currentY;
		return this.enabled && mouseX >= x && mouseY >= y && mouseX < x + this.width && mouseY < y + this.height;
	}
}
