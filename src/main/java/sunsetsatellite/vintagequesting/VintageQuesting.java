package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.vintagequesting.command.commands.CommandQuest;
import sunsetsatellite.vintagequesting.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.registry.TaskRegistry;
import turniplabs.halplibe.HalpLibe;


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
		LOGGER.info("Vintage Questing initialized.");
	}

}
