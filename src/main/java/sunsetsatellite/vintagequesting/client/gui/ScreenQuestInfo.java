package sunsetsatellite.vintagequesting.client.gui;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;

import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.client.RewardRendererDispatcher;
import sunsetsatellite.vintagequesting.client.TaskRenderDispatcher;
import sunsetsatellite.vintagequesting.client.gui.generic.MessageBoxElement;
import sunsetsatellite.vintagequesting.client.gui.generic.VerticalContainerElement;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageSubmitQuests;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.List;

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

		drawLineVertical(width / 2, 24, height - 32, 0xFFFFFFFF);

		drawStringCenteredShadow(fontRenderer, quest.getTranslatedName(), width / 2, 8, 0xFFFFFFFF);
		drawStringCenteredShadow(fontRenderer, "Rewards:", width / 4, height / 2 + 10, 0xFFFFFFFF);
		if (quest.getRewards().isEmpty()) {
			drawStringCenteredShadow(fontRenderer, "No rewards :(", width / 4, height / 2 + 34, 0xFF808080);
		}
		drawStringCenteredShadow(fontRenderer, "Tasks (" + quest.getTaskLogic() + "):", width - (width / 4) - 8, 24, 0xFFFFFFFF);
		if (quest.getTasks().isEmpty()) {
			drawStringCenteredShadow(fontRenderer, "No tasks.", width - (width / 4), 48, 0xFF808080);
		}

		messageBox.render(8, 24, mouseX, mouseY);

		rewardContainer.render(8, height / 2 + 12, mouseX, mouseY);
		taskContainer.render(width / 2 + 12, 36, mouseX, mouseY);

		super.render(mouseX, mouseY, partialTick);
	}

	@Override
	public void init() {
		messageBox = new MessageBoxElement(width / 2 - 24, height / 3 + 24,
			quest.getTranslatedDescription(),
			((width / 2 - 24) / fontRenderer.getFont().charWidth('m')) + 4);
		rewardContainer = new VerticalContainerElement(width / 2 - 24, height / 3, 8);
		taskContainer = new VerticalContainerElement(width / 2 - 24, height - (48 * 2) + 3, 8);
		List<Reward> rewards = quest.getRewards();
		for (int i = 0; i < rewards.size(); i++) {
			Reward reward = rewards.get(i);
			RewardRendererDispatcher.getInstance().getDispatch(reward.getClass()).renderSlot(parent.getCurrentPage().chapter,quest,reward, mc, rewardContainer.renderables, i, width);
		}
		List<Task> tasks = quest.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			TaskRenderDispatcher.getInstance().getDispatch(task.getClass()).renderSlot(parent.getCurrentPage().chapter,quest,task, mc, taskContainer.renderables, i, width);
		}

		buttons.add(new ButtonElement(0, width / 2 - 30, height - 24, 60, 20, "Back"));
		buttons.add(claimButton = new ButtonElement(1, width / 4 - 120, height - 24, 200, 20, "Claim"));
		buttons.add(submitButton = new ButtonElement(2, width - (width / 4) - 80, height - 24, 200, 20, "Submit"));
		super.init();
	}

	@Override
	public void mouseReleased(int mx, int my, int buttonNum) {
		messageBox.mouseMovedOrUp(mx, my, buttonNum);
		taskContainer.mouseMovedOrUp(mx, my, buttonNum);
		super.mouseReleased(mx, my, buttonNum);
	}


	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		messageBox.onClick(mouseX, mouseY, mouseButton);
		taskContainer.mouseClicked(mouseX, mouseY, mouseButton);
		rewardContainer.mouseClicked(mouseX, mouseY, mouseButton);

		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void buttonClicked(ButtonElement button) {
		if (button.id == 0) {
			this.mc.displayScreen(getParentScreen());
		} else if (button == claimButton) {
			for (Reward reward : quest.getRewards()) {
				reward.give(mc.thePlayer);
			}
		} else if (button == submitButton) {
			if(EnvironmentHelper.isMultiplayerClient()){
				NetworkHandler.sendToServer(new NetworkMessageSubmitQuests());
			}
			VintageQuesting.submitQuests(mc.thePlayer);
		}
	}

	@Override
	public void tick() {
		claimButton.enabled = quest.isCompleted() && quest.preRequisitesCompleted() && !quest.areAllRewardsRedeemed();
		if (!quest.preRequisitesCompleted()) {
			claimButton.displayString = "Prerequisites not completed!";
		} else {
			claimButton.displayString = "Claim";
		}
		if (mc.currentWorld.isClientSide) {
			claimButton.enabled = false;
			claimButton.displayString = "Can't claim in multiplayer yet.";
		}
		submitButton.enabled = !quest.isCompleted();
	}
}
