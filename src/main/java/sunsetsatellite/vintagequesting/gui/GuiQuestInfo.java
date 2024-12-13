package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.generic.GuiMessageBox;
import sunsetsatellite.vintagequesting.gui.generic.GuiVerticalContainer;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;
import sunsetsatellite.vintagequesting.quest.task.VisitDimensionTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GuiQuestInfo extends GuiScreen {

	protected Quest quest;
	protected GuiQuestbook parent;
	protected GuiMessageBox messageBox;
	protected GuiVerticalContainer rewardContainer;
	protected GuiVerticalContainer taskContainer;
	protected GuiButton claimButton;
	protected GuiButton submitButton;

	public GuiQuestInfo(GuiQuestbook parent, Quest quest) {
		super(parent);
		this.quest = quest;
		this.parent = parent;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawRectWidthHeight(0,0,width,height,0xFF404040);
		GL11.glEnable(3553);

		drawLineVertical(width/2,24,height-32,0xFFFFFFFF);

		drawStringCentered(fontRenderer,quest.getTranslatedName(),width/2,8,0xFFFFFFFF);
		drawStringCentered(fontRenderer,"Rewards:",width / 4,height / 2 + 10,0xFFFFFFFF);
		if(quest.getRewards().isEmpty()){
			drawStringCentered(fontRenderer,"No rewards :(",width / 4,height / 2 + 34,0xFF808080);
		}
		drawStringCentered(fontRenderer,"Tasks:",width - (width / 4) - 8 ,24,0xFFFFFFFF);
		if(quest.getTasks().isEmpty()){
			drawStringCentered(fontRenderer,"No tasks.",width - (width / 4) ,48,0xFF808080);
		}

		messageBox.render(8,24,mouseX,mouseY);

		rewardContainer.render(8,height / 2 + 12,mouseX,mouseY);
		taskContainer.render(width / 2 + 12 ,36,mouseX,mouseY);

		super.drawScreen(mouseX, mouseY, partialTick);
	}

	@Override
	public void init() {
		messageBox = new GuiMessageBox(width / 2 - 24,height / 3 + 24,
			quest.getTranslatedDescription(),
			((width / 2 - 24) / fontRenderer.getCharWidth('m')) + 4);
		rewardContainer = new GuiVerticalContainer(width / 2 - 24,height / 3,8);
		taskContainer = new GuiVerticalContainer(width / 2 - 24,height - (48*2) + 3,8);
		for (Reward reward : quest.getRewards()) {
			reward.renderSlot(mc,rewardContainer.renderables,width);
		}
		List<Task> tasks = quest.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			task.renderSlot(mc,taskContainer.renderables,i,width);
		}

		controlList.add(new GuiButton(0,width/2 - 30, height-24, 60, 20, "Back"));
		controlList.add(claimButton = new GuiButton(1,width / 4 - 120, height-24, 200, 20, "Claim"));
		controlList.add(submitButton = new GuiButton(2,width - (width / 4) - 80, height-24, 200, 20, "Submit"));
		super.init();
	}

	@Override
	public void mouseMovedOrButtonReleased(int mouseX, int mouseY, int mouseButton) {
		messageBox.mouseMovedOrUp(mouseX,mouseY,mouseButton);

		super.mouseMovedOrButtonReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		messageBox.onClick(mouseX,mouseY,mouseButton);
		taskContainer.mouseClicked(mouseX,mouseY,mouseButton);
		rewardContainer.mouseClicked(mouseX,mouseY,mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void buttonPressed(GuiButton button) {
		if(button.id == 0){
			this.mc.displayGuiScreen(getParentScreen());
		} else if (button == claimButton) {
			for (Reward reward : quest.getRewards()) {
				reward.give(mc.thePlayer);
			}
		} else if (button == submitButton) {
			ArrayList<ItemStack> stacks = VintageQuesting.condenseItemList(Arrays.stream(mc.thePlayer.inventory.mainInventory).collect(Collectors.toList()));
			for (Chapter chapter : ((IHasQuests) mc.thePlayer).getQuestGroup().chapters) {
				for (Quest chapterQuest : chapter.getQuests()) {
					if(chapterQuest.isCompleted()) continue;
					for (Task task : chapterQuest.getTasks()) {
						if(task instanceof RetrievalTask){
							((RetrievalTask) task).resetProgress();
							for (ItemStack stack : stacks) {
								((RetrievalTask) task).setProgress(stack,parent.player);
							}
						} else if (task instanceof VisitDimensionTask) {
							((VisitDimensionTask) task).check(parent.player);
						}
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		claimButton.enabled = quest.isCompleted() && quest.preRequisitesCompleted() && !quest.areAllRewardsRedeemed();
		if(!quest.preRequisitesCompleted()){
			claimButton.displayString = "Prerequisites not completed!";
		} else {
			claimButton.displayString = "Claim";
		}
		submitButton.enabled = !quest.isCompleted();
	}
}
