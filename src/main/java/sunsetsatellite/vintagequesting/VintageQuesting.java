package sunsetsatellite.vintagequesting;

import com.mojang.nbt.CompoundTag;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.vintagequesting.gui.GuiQuestbook;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;
import sunsetsatellite.vintagequesting.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.registry.QuestRegistry;
import sunsetsatellite.vintagequesting.registry.RewardRegistry;
import sunsetsatellite.vintagequesting.registry.TaskRegistry;
import turniplabs.halplibe.helper.gui.factory.base.GuiFactory;
import turniplabs.halplibe.helper.gui.registered.RegisteredGui;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.*;


public class VintageQuesting implements ModInitializer, ClientStartEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "vintagequesting";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ChapterRegistry CHAPTERS = new ChapterRegistry();
	public static QuestRegistry QUESTS = new QuestRegistry();
	public static RewardRegistry REWARDS = new RewardRegistry();
	public static TaskRegistry TASKS = new TaskRegistry();

	public static final RegisteredGui QUESTBOOK = new RegisteredGui(MOD_ID, "questbook", new GuiFactory() {
		@Override
		public @NotNull GuiScreen createGui(@NotNull RegisteredGui gui, @NotNull EntityPlayerSP player) {
			return new GuiQuestbook(player,null);
		}

		@Override
		public @Nullable Container createContainer(@NotNull RegisteredGui gui, @NotNull EntityPlayerMP player) {
			return null;
		}
	}, true);

	public static CompoundTag playerData;

	@Override
    public void onInitialize() {

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
		LOGGER.info("Vintage Questing initialized.");
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

	public static ArrayList<ItemStack> condenseItemList(List<ItemStack> list) {
		ArrayList<ItemStack> stacks = new ArrayList<>();
		for (ItemStack stack : list) {
			if (stack != null) {
				Optional<ItemStack> existing = stacks.stream().filter((S) -> S.itemID == stack.itemID).findAny();
				if (existing.isPresent()) {
					existing.get().stackSize += stack.stackSize;
				} else {
					stacks.add(stack.copy());
				}
			}
		}
		return stacks;
	}
}
