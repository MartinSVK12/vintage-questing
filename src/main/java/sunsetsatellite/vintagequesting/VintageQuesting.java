package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
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
import sunsetsatellite.vintagequesting.util.Logic;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.*;


public class VintageQuesting implements ModInitializer, ClientStartEntrypoint, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "vintagequesting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final List<Chapter> chapters = new ArrayList<>();
	public static GuiChapterButton currentChapter = null;
    @Override
    public void onInitialize() {
        LOGGER.info("Vintage Questing initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
		ItemReward reward = new ItemReward(new ItemStack(Item.diamond,1));
		ClickTask task = new ClickTask();
		RetrievalTask task2 = new RetrievalTask(new ItemStack(Block.netherrackIgneous,16));
		Quest testQuest = new Quest("quest.vq.test","quest.vq.test", Block.blockDiamond, Logic.AND,Logic.AND).setRewards(Collections.singletonList(reward)).setTasks(listOf(task,task2));
		Quest testQuest2 = new Quest("quest.vq.test2","quest.vq.test2", Block.dirt, Logic.AND,Logic.AND).setX(128).setY(-64).setPreRequisites(Collections.singletonList(testQuest)).setTasks(Collections.singletonList(task.copy()));
		ArrayList<Quest> list = new ArrayList<>();
		list.add(testQuest);
		list.add(testQuest2);
		Chapter chapter = new Chapter(Block.grass,"chapter.vq.test","chapter.vq.test", list);
		Chapter chapter2 = new Chapter(Item.foodAppleGold,"chapter.vq.test2","chapter.vq.test2", new ArrayList<>());
		chapters.add(chapter);
		chapters.add(chapter2);
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
