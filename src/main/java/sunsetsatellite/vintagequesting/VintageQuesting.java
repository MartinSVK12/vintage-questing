package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.vintagequesting.command.commands.CommandQuest;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;
import sunsetsatellite.vintagequesting.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.registry.TaskRegistry;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class VintageQuesting implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, ClientStartEntrypoint {
    public static final String MOD_ID = "vintage-questing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ChapterRegistry CHAPTERS = new ChapterRegistry();
	public static QuestRegistry QUESTS = new QuestRegistry();
	public static RewardRegistry REWARDS = new RewardRegistry();
	public static TaskRegistry TASKS = new TaskRegistry();

	public static void registerServerCommands() {
	}

	public static void registerClientCommands() {
		System.out.println("Registered Client Commands");
		if (EnvironmentHelper.isSinglePlayer()) {
			CommandManager.registerCommand(new CommandQuest());
		}
	}

	@Override
    public void onInitialize() {
        LOGGER.info("Vintage Questing initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.vintagequesting");
		IKeybinds keybinds = (IKeybinds) Minecraft.getMinecraft().gameSettings;
		category
			.withComponent(new KeyBindingComponent(keybinds.vintage_questing$getKeyOpenQuestbook()));
		OptionsPages.CONTROLS
			.withComponent(category);
	}
}
