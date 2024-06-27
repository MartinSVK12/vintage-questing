package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.GuiQuestbook;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;

@Mixin(value = Minecraft.class,remap = false)
public abstract class MinecraftMixin {

	@Shadow
	public GameSettings gameSettings;
	@Shadow
	public GuiScreen currentScreen;

	@Shadow
	public abstract void displayGuiScreen(GuiScreen guiscreen);

	@Shadow
	public EntityPlayerSP thePlayer;
	@Unique
	private static int debounce = 0;

	@Inject(
		method = "runTick",
		at = @At(value = "INVOKE",target = "Lorg/lwjgl/input/Keyboard;next()Z",shift = At.Shift.AFTER)
	)
	public void handleKeyboard(CallbackInfo ci) {
		if (debounce > 0) debounce--;
		boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
		boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
		IKeybinds keybinds = ((IKeybinds) gameSettings);
		KeyBinding openQuestbook = keybinds.vintage_questing$getKeyOpenQuestbook();
		if(debounce <= 0){
			if(openQuestbook.isPressed() && currentScreen == null){
				VintageQuesting.QUESTBOOK.open(this.thePlayer);
				debounce = 10;
			}
		}
	}
}
