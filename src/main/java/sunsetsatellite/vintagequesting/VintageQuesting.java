package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.vintagequesting.gui.GuiChapterButton;
import sunsetsatellite.vintagequesting.gui.GuiQuestButton;
import sunsetsatellite.vintagequesting.gui.GuiQuestbook;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.reward.ItemReward;
import sunsetsatellite.vintagequesting.quest.task.ClickTask;
import sunsetsatellite.vintagequesting.quest.task.RetrievalTask;
import sunsetsatellite.vintagequesting.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.registry.TaskRegistry;
import sunsetsatellite.vintagequesting.util.Logic;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.*;


public class VintageQuesting implements ModInitializer, ClientStartEntrypoint, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "vintagequesting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static GuiChapterButton currentChapter = null;

	public static final ChapterRegistry CHAPTERS = new ChapterRegistry();
	public static final QuestRegistry QUESTS = new QuestRegistry();
	public static final RewardRegistry REWARDS = new RewardRegistry();
	public static final TaskRegistry TASKS = new TaskRegistry();

    @Override
    public void onInitialize() {
        LOGGER.info("Vintage Questing initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		Registries.getInstance().register("vintagequesting:chapters",CHAPTERS);
		Registries.getInstance().register("vintagequesting:quests",QUESTS);
		Registries.getInstance().register("vintagequesting:rewards", REWARDS);
        Registries.getInstance().register("vintagequesting:tasks", TASKS);
	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.vintagequesting");
		IKeybinds keybinds = (IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings;
		category
			.withComponent(new KeyBindingComponent(keybinds.vintage_questing$getKeyOpenQuestbook()));
		OptionsPages.CONTROLS
			.withComponent(category);
	}

	public static <K,V> Map<K,V> mapOf(K[] keys, V[] values){
		if(keys.length != values.length){
			throw new IllegalArgumentException("Arrays differ in size!");
		}
		HashMap<K,V> map = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i],values[i]);
		}
		return map;
	}

	public static <T,V> T[] arrayFill(T[] array,V value){
		Arrays.fill(array,value);
		return array;
	}

	@SafeVarargs
	public static <T> List<T> listOf(T... values){
		return new ArrayList<>(Arrays.asList(values));
	}

	public static <T,U> List<Pair<T,U>> zip(List<T> first, List<U> second){
		List<Pair<T,U>> list = new ArrayList<>();
		List<?> shortest = first.size() < second.size() ? first : second;
		for (int i = 0; i < shortest.size(); i++) {
			list.add(Pair.of(first.get(i),second.get(i)));
		}
		return list;
	}
}
