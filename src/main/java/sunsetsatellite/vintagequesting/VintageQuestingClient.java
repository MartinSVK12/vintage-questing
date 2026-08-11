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
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.dependency.Key;

public class VintageQuestingClient implements ClientModInitializer {

	public static KeyBinding keyOpenQuestbook = new KeyBinding("key.vintagequesting.openQuestbook").bind(InputDevice.keyboard, Keyboard.KEY_GRAVE);

	public static ChapterPageRegistry CHAPTER_PAGES = new ChapterPageRegistry();

	@Override
	public void onInitializeClient() {
		GameSettings.register(keyOpenQuestbook);
		ClientEvents.AFTER_CLIENT_START.listen(Key.of(VintageQuesting.MOD_ID), this::afterClientStart);
	}

	public void afterClientStart() {
		OptionsCategory category = new OptionsCategory("gui.options.page.controls.category.vintagequesting");
		category
			.withComponent(new KeyBindingComponent(keyOpenQuestbook));
		OptionsPages.CONTROLS
			.withComponent(category);
	}
}
