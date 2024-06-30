package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.entity.player.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.generic.GuiMessageBox;
import sunsetsatellite.vintagequesting.gui.generic.GuiVerticalContainer;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;
import java.util.ArrayList;
import java.util.List;

public class GuiQuestbook extends GuiScreen {

	protected int lastMouseX = 0;
	protected int lastMouseY = 0;
	protected int currentX = 0;
	protected int currentY = 0;
	protected boolean isMouseButtonDown = false;
	protected List<GuiQuestButton> currentQuests = new ArrayList<>();
	protected Chapter currentChapter;
	protected GuiTooltip tooltip;
	private boolean init = false;
	public final EntityPlayer player;
	protected GuiVerticalContainer chapterContainer;
	protected GuiMessageBox descriptionBox;
	protected GuiButton descButton;
	protected GuiButton closeButton;
	protected boolean showDesc;


	public GuiQuestbook(EntityPlayer player, GuiScreen parent) {
		super(parent);
		this.player = player;
		if(((IHasQuests) player).getCurrentChapter() != null){
			this.currentChapter = ((IHasQuests) player).getCurrentChapter();
			loadChapter(currentChapter);
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		drawRectWidthHeight(0,0,width,height,0xFF808080);
		GL11.glEnable(3553);
		GL11.glDisable(GL11.GL_BLEND);

		if(currentChapter != null) {
			drawChapter(mouseX, mouseY, partialTick);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		drawRectWidthHeight(0,0,160,height,0xFF404040);
		GL11.glEnable(3553);
		GL11.glDisable(GL11.GL_BLEND);

		if(Mouse.isButtonDown(2)){
			currentX = 0;
			currentY = 0;
		}

		if (Mouse.isButtonDown(0)) {
			if(!isMouseButtonDown){
				this.isMouseButtonDown = true;
			} else {
				int mxd = mouseX - this.lastMouseX;
				int myd = mouseY - this.lastMouseY;
				currentX += mxd;
				currentY += myd;
			}
			this.lastMouseX = mouseX;
			this.lastMouseY = mouseY;
		} else {
			this.isMouseButtonDown = false;
		}


		if(mc.isDebugInfoEnabled()){
			drawString(fontRenderer,String.format("WX: %d | WY: %d | CX: %d | CY: %d | WW: %d | WH: %d | Pressing: %b | CC: %s | CQ: %s",mouseX,mouseY, currentX, currentY,width,height,isMouseButtonDown, currentChapter != null ? currentChapter.getTranslatedName() : "null", getQuestAtPosition(mouseX,mouseY) != null ? getQuestAtPosition(mouseX,mouseY).quest.getTranslatedName() : "null") ,2,2,0xFFFFFFFF);
		}

		drawString(fontRenderer,"Questbook",48,12,0xFFFFFFFF);

		if(currentChapter != null){
			drawString(fontRenderer,String.format("%d / %d completed.",currentChapter.numberOfCompletedQuests(),currentChapter.getQuests().size()),168,10,0xFFFFFFFF);
		}

		if(showDesc){
			chapterContainer.setHeight((height - 48)/2);
			descriptionBox.render(0,((height - 48)/2) + 24,mouseX,mouseY);
		} else {
			chapterContainer.setHeight(height - 48);
		}
		chapterContainer.render(0,24,mouseX,mouseY);

		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	public void init() {
		super.init();

		chapterContainer = new GuiVerticalContainer(160,height - 48,4);
		String s = "No description available.\n\nMaybe try selecting a\nchapter?";
		descriptionBox = new GuiMessageBox(160,((height - 48)/2) - 2,s,160 / fontRenderer.getCharWidth('m'));

		descButton = new GuiButton(0,8,height - 24,20,20,"D");
		controlList.add(descButton);

		for (int i = 1; i <= 4; i++) {
			GuiButton yetUnusedButton = new GuiButton(i,(i * 20) + 8 + (i * 8),height - 24,20,20,"?");
			yetUnusedButton.enabled = false;
			controlList.add(yetUnusedButton);
		}

		closeButton = new GuiButton(5,width - 24,4,20,20,"X");
		controlList.add(closeButton);

		int id = 0;
		for (Chapter chapter : ((IHasQuests) player).getQuestGroup().chapters) {
			GuiChapterButton chapterButton = new GuiChapterButton(id,chapter,24,24 + (id * 20),120,20);
			chapterContainer.renderables.add(chapterButton);
			id++;
		}
	}

	@Override
	protected void buttonPressed(GuiButton button) {
		super.buttonPressed(button);
		if(button == descButton){
			showDesc = !showDesc;
		}
		if(button == closeButton){
			if(getParentScreen() != null){
				mc.displayGuiScreen(getParentScreen());
			} else {
				mc.displayGuiScreen(null);
			}
		}
		if(button instanceof GuiChapterButton){
			loadChapter(((GuiChapterButton) button).chapter);
		} else if (button instanceof GuiQuestButton) {
			mc.displayGuiScreen(new GuiQuestInfo(this,((GuiQuestButton) button).quest));
		}
	}

	private void loadChapter(Chapter chapter){
		this.currentChapter = chapter;
		currentQuests.clear();
		((IHasQuests) player).setCurrentChapter(chapter);
		int id = 0;
		for (Quest quest : chapter.getQuests()) {
			GuiQuestButton questButton = new GuiQuestButton(this, id, quest);
			currentQuests.add(questButton);
		}
		if(currentChapter != null && descriptionBox != null){
			String s = currentChapter.getTranslatedDescription();
			descriptionBox.setText(s);
		}
	}

	private void drawChapter(int mouseX, int mouseY, float partialTick) {
		if(tooltip == null){
			tooltip = new GuiTooltip(mc);
		}
		for (GuiQuestButton questButton : currentQuests) {
			Quest quest = questButton.getQuest();
			if(!quest.getPreRequisites().isEmpty()){
				int x = (quest.getX()+(quest.getWidth()/2))+currentX;
				int y = (quest.getY()+(quest.getHeight()/2))+currentY;
				for (Quest preRequisite : quest.getPreRequisites()) {
					int x2 = (preRequisite.getX()+(preRequisite.getWidth()/2))+currentX;
					int y2 = (preRequisite.getY()+(preRequisite.getHeight()/2))+currentY;

					if(quest.isCompleted() && preRequisite.isCompleted()){
						drawLineHorizontal(x,x2,y2,0xFF00CC00);
						drawLineVertical(x,y,y2,0xFF00CC00);
					} else {
						drawLineHorizontal(x,x2,y2,0xFFCC0000);
						drawLineVertical(x,y,y2,0xFFCC0000);
					}

				}
			}
		}
		for (GuiQuestButton quest : currentQuests) {
			quest.drawButton(mc,mouseX,mouseY);
		}
		GuiQuestButton questButton;
		if((questButton = getQuestAtPosition(mouseX,mouseY)) != null){
			Quest quest = questButton.getQuest();
			GL11.glPushMatrix();
			GL11.glTranslatef(0,0,2);
			StringBuilder s = new StringBuilder(quest.getTranslatedName());
			if(quest.getPreRequisites().isEmpty() || quest.preRequisitesCompleted()){
				if(quest.isCompleted()){
					s.append("\n").append(TextFormatting.LIME).append("Completed!");
					if(!quest.areAllRewardsRedeemed()){
						s.append("\n").append(TextFormatting.LIGHT_BLUE).append("Unclaimed rewards!");
					}
				} else {
					s.append("\n").append(TextFormatting.LIGHT_GRAY).append(quest.numberOfCompletedTasks()).append("/").append(quest.getTasks().size()).append(" tasks.");
				}
			} else {
				s.append("\n").append(TextFormatting.RED).append("Requires ").append("(").append(quest.getQuestLogic()).append("):");
				for (Quest preRequisite : quest.getPreRequisites()) {
					s.append("\n").append(TextFormatting.RED).append("- ").append(preRequisite.getTranslatedName());
				}
				s.append(TextFormatting.WHITE);
			}

			tooltip.render(s.toString(),mouseX,mouseY,8,-8);
			GL11.glPopMatrix();
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		if(mouseButton == 0)
		{
			for (IRenderable renderable : chapterContainer.renderables) {
				GuiButton guiButton = (GuiButton) renderable;
				if (guiButton.mouseClicked(mc, mouseX, mouseY) && guiButton.playSound) {
					selectedButton = guiButton;
					mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
					if (guiButton.listener != null) {
						guiButton.listener.listen(guiButton);
					} else {
						buttonPressed(guiButton);
					}
					return;
				}
			}

			for (GuiButton guiButton : controlList) {
				if (guiButton.mouseClicked(mc, mouseX, mouseY) && guiButton.playSound) {
					selectedButton = guiButton;
					mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
					if (guiButton.listener != null) {
						guiButton.listener.listen(guiButton);
					} else {
						buttonPressed(guiButton);
					}
					return;
				}
			}

			for (GuiQuestButton guiButton : currentQuests) {
				if (guiButton.mouseClicked(mc, mouseX, mouseY) && guiButton.playSound) {
					selectedButton = guiButton;
					mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
					if (guiButton.listener != null) {
						guiButton.listener.listen(guiButton);
					} else {
						buttonPressed(guiButton);
					}
					return;
				}
			}

		}
	}

	public void mouseMovedOrButtonReleased(int mouseX, int mouseY, int mouseButton)
	{
		if(selectedButton != null && mouseButton == 0)
		{
			buttonReleased(selectedButton);
			selectedButton.mouseReleased(mouseX, mouseY);
			selectedButton = null;
		}
	}

	public boolean getIsMouseOverQuest(GuiQuestButton quest, int mouseX, int mouseY) {

		int questX = quest.xPosition+currentX;
		int questY = quest.yPosition+currentY;

		return mouseX >= questX - 1 && mouseX < questX + quest.getWidth() + 1 && mouseY >= questY - 1 && mouseY < questY + quest.getHeight() + 1;
	}

	public GuiQuestButton getQuestAtPosition(int mouseX, int mouseY) {
		for (GuiQuestButton questButton : this.currentQuests) {
			if (this.getIsMouseOverQuest(questButton, mouseX, mouseY)) {
				return questButton;
			}
		}
		return null;
	}

	@Override
	public void tick() {
		if(!init){
			init = true;
			currentX += width/2;
			currentY += height/2;
		}
	}


}
