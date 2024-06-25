package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.gui.generic.GuiMessageBox;
import sunsetsatellite.vintagequesting.gui.generic.GuiString;
import sunsetsatellite.vintagequesting.gui.generic.GuiVerticalContainer;
import sunsetsatellite.vintagequesting.gui.slot.reward.GuiItemRewardSlot;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiClickTaskSlot;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiRetrievalTaskSlot;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;
import sunsetsatellite.vintagequesting.quest.task.ClickTask;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;

import java.util.List;

public class GuiQuestInfo extends GuiScreen {

	protected Quest quest;
	protected GuiMessageBox messageBox;
	protected GuiVerticalContainer rewardContainer;
	protected GuiVerticalContainer taskContainer;
	protected GuiButton claimButton;
	protected GuiButton submitButton;

	public GuiQuestInfo(GuiQuestbook parent, Quest quest) {
		super(parent);
		this.quest = quest;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawRectWidthHeight(0,0,width,height,0xFF404040);
		GL11.glEnable(3553);

		drawLineVertical(width/2,24,height-32,0xFFFFFFFF);

		drawStringCentered(fontRenderer,quest.getTranslatedName(),width/2,8,0xFFFFFFFF);
		drawStringCentered(fontRenderer,"Rewards:",width / 4,height / 2,0xFFFFFFFF);
		if(quest.getRewards().isEmpty()){
			drawStringCentered(fontRenderer,"No rewards :(",width / 4,height / 2 + 24,0xFF808080);
		}
		drawStringCentered(fontRenderer,"Tasks:",width - (width / 4) ,24,0xFFFFFFFF);
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
			if(reward instanceof ItemReward){
				rewardContainer.renderables.add(new GuiItemRewardSlot(mc,width / 2 - 38,24, (ItemReward) reward));
			}
		}
		List<Task> tasks = quest.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			if (task instanceof ClickTask) {
				taskContainer.renderables.add(new GuiString(mc, (i+1)+". "+task.getTranslatedTypeName(), 0xFFFFFFFF));
				taskContainer.renderables.add(new GuiClickTaskSlot(mc, width / 2 - 48, 20, true, (ClickTask) task));
			} else if (task instanceof RetrievalTask) {
				taskContainer.renderables.add(new GuiString(mc, (i+1)+". "+task.getTranslatedTypeName()+" | Consume: "+task.canConsume(), 0xFFFFFFFF));
				taskContainer.renderables.add(new GuiRetrievalTaskSlot(mc, width / 2 - 48, 24, (RetrievalTask) task));
			}
		}

		controlList.add(new GuiButton(0,width/2 - 30, height-24, 60, 20, "Back"));
		controlList.add(claimButton = new GuiButton(1,width / 4 - 120, height-24, 240, 20, "Claim"));
		controlList.add(submitButton = new GuiButton(2,width - (width / 4) - 120, height-24, 240, 20, "Submit"));
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
			for (Task task : quest.getTasks()) {
				if(task instanceof RetrievalTask){
					for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
						int addedProgress = ((RetrievalTask) task).addProgress(stack);
						if(addedProgress > 0 && task.canConsume()){
							stack.stackSize -= addedProgress;
						}
					}
				}
			}
		}
	}

	@Override
	public void tick() {
		claimButton.enabled = quest.isCompleted() && !quest.areAllRewardsRedeemed();
		submitButton.enabled = !quest.isCompleted();
	}
}
