package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.command.commands.CommandQuest;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.RetrievalTask;
import sunsetsatellite.vintagequesting.core.instance.task.VisitDimensionTask;
import sunsetsatellite.vintagequesting.core.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.core.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.core.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.core.registry.TaskRegistry;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageCompleteClickTask;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageQuestSync;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageSubmitQuests;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;


public class VintageQuesting implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("vintagequesting", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ChapterRegistry CHAPTERS = new ChapterRegistry();
	public static QuestRegistry QUESTS = new QuestRegistry();
	public static RewardRegistry REWARDS = new RewardRegistry();
	public static TaskRegistry TASKS = new TaskRegistry();

	@Override
	public void onInitialize() {
		CommandManager.registerCommand(new CommandQuest());
		NetworkHandler.registerNetworkMessage(NetworkMessageQuestSync::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageSubmitQuests::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageCompleteClickTask::new);
		LOGGER.info("Vintage Questing initialized.");
	}

	public static void submitQuests(Player player) {
		ArrayList<ItemStack> stacks = Catalyst.condenseItemList(Arrays.stream(player.inventory.mainInventory).collect(Collectors.toList()));
		for (Chapter chapter : VintageQuesting.CHAPTERS) {
			for (Quest chapterQuest : chapter.getQuests()) {
				if (chapterQuest.isCompleted()) continue;
				for (Task task : chapterQuest.getTasks()) {
					if (task instanceof RetrievalTask) {
						((RetrievalTask) task).resetProgress();
						for (ItemStack stack : stacks) {
							((RetrievalTask) task).setProgress(stack, player);
						}
					} else if (task instanceof VisitDimensionTask) {
						((VisitDimensionTask) task).check(player);
					}
				}
			}
		}
	}
}
