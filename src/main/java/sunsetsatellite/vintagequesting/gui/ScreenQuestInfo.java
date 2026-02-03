package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.generic.MessageBoxElement;
import sunsetsatellite.vintagequesting.gui.generic.VerticalContainerElement;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;
import sunsetsatellite.vintagequesting.quest.task.VisitDimensionTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenQuestInfo extends Screen {

	protected Quest quest;
	protected ScreenQuestbook parent;
	protected MessageBoxElement messageBox;
	protected VerticalContainerElement rewardContainer;
	protected VerticalContainerElement taskContainer;
	protected ButtonElement claimButton;
	protected ButtonElement submitButton;

	public ScreenQuestInfo(ScreenQuestbook parent, Quest quest) {
		super(parent);
		this.quest = quest;
		this.parent = parent;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTick) {
		renderTexturedBackground();
		//drawRectWidthHeight(0,0,width,height,0xFF404040);
		GL11.glEnable(3553);

		drawLineVertical(width/2,24,height-32,0xFFFFFFFF);

		drawStringCentered(font,quest.getTranslatedName(),width/2,8,0xFFFFFFFF);
		drawStringCentered(font,"Rewards:",width / 4,height / 2 + 10,0xFFFFFFFF);
		if(quest.getRewards().isEmpty()){
			drawStringCentered(font,"No rewards :(",width / 4,height / 2 + 34,0xFF808080);
		}
		drawStringCentered(font,"Tasks ("+quest.getTaskLogic()+"):",width - (width / 4) - 8 ,24,0xFFFFFFFF);
		if(quest.getTasks().isEmpty()){
			drawStringCentered(font,"No tasks.",width - (width / 4) ,48,0xFF808080);
		}

		messageBox.render(8,24,mouseX,mouseY);

		rewardContainer.render(8,height / 2 + 12,mouseX,mouseY);
		taskContainer.render(width / 2 + 12 ,36,mouseX,mouseY);

		super.render(mouseX, mouseY, partialTick);
	}

	@Override
	public void init() {
		messageBox = new MessageBoxElement(width / 2 - 24,height / 3 + 24,
			quest.getTranslatedDescription(),
			((width / 2 - 24) / font.getCharWidth('m')) + 4);
		rewardContainer = new VerticalContainerElement(width / 2 - 24,height / 3,8);
		taskContainer = new VerticalContainerElement(width / 2 - 24,height - (48*2) + 3,8);
		for (Reward reward : quest.getRewards()) {
			reward.renderSlot(mc,rewardContainer.renderables,width);
		}
		List<Task> tasks = quest.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			task.renderSlot(mc,taskContainer.renderables,i,width);
		}

		buttons.add(new ButtonElement(0,width/2 - 30, height-24, 60, 20, "Back"));
		buttons.add(claimButton = new ButtonElement(1,width / 4 - 120, height-24, 200, 20, "Claim"));
		buttons.add(submitButton = new ButtonElement(2,width - (width / 4) - 80, height-24, 200, 20, "Submit"));
		super.init();
	}

	@Override
	public void mouseReleased(int mx, int my, int buttonNum) {
		messageBox.mouseMovedOrUp(mx,my,buttonNum);
		taskContainer.mouseMovedOrUp(mx,my,buttonNum);
		super.mouseReleased(mx, my, buttonNum);
	}


	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		messageBox.onClick(mouseX,mouseY,mouseButton);
		taskContainer.mouseClicked(mouseX,mouseY,mouseButton);
		rewardContainer.mouseClicked(mouseX,mouseY,mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void buttonClicked(ButtonElement button) {
		if(button.id == 0){
			this.mc.displayScreen(getParentScreen());
		} else if (button == claimButton) {
			for (Reward reward : quest.getRewards()) {
				reward.give(mc.thePlayer);
			}
		} else if (button == submitButton) {
			ArrayList<ItemStack> stacks = Catalyst.condenseItemList(Arrays.stream(mc.thePlayer.inventory.mainInventory).collect(Collectors.toList()));
			for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
				for (Quest chapterQuest : chapter.getQuests()) {
					if(chapterQuest.isCompleted()) continue;
					for (Task task : chapterQuest.getTasks()) {
						if(task instanceof RetrievalTask){
							((RetrievalTask) task).resetProgress();
							for (ItemStack stack : stacks) {
								((RetrievalTask) task).setProgress(stack, mc.thePlayer);
							}
						} else if (task instanceof VisitDimensionTask) {
							((VisitDimensionTask) task).check(mc.thePlayer);
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
		if(mc.currentWorld.isClientSide){
			claimButton.enabled = false;
			claimButton.displayString = "Can't claim in multiplayer yet.";
		}
		submitButton.enabled = !quest.isCompleted();
	}
}
