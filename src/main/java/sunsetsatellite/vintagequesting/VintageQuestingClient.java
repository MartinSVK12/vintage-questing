package sunsetsatellite.vintagequesting;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.options.components.KeyBindingComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import sunsetsatellite.vintagequesting.core.registry.ChapterPageRegistry;
import sunsetsatellite.vintagequesting.core.registry.ChapterRegistry;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VintageQuestingClient implements ClientModInitializer {

	public static KeyBinding keyOpenQuestbook = new KeyBinding("key.vintagequesting.openQuestbook").bind(InputDevice.keyboard, Keyboard.KEY_GRAVE);

	public static QuestTeam LOCAL_TEAM = null;
	public static ChapterPageRegistry CHAPTER_PAGES = new ChapterPageRegistry();

	@Override
	public void onInitializeClient() {
		GameSettings.register(keyOpenQuestbook);
		ClientEvents.AFTER_CLIENT_START.listen(Key.of(VintageQuesting.MOD_ID), this::afterClientStart);
	}

	public static void reloadPages(){
		VintageQuestingClient.CHAPTER_PAGES.forEach((C)->{
			C.chapter = VintageQuestingClient.LOCAL_TEAM.chapters.get(C.chapter.getId());
		});
	}

	public void afterClientStart() {
		OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.vintagequesting");
		category
			.withComponent(new KeyBindingComponent(keyOpenQuestbook));
		OptionsPages.CONTROLS
			.withComponent(category);
	}
}
