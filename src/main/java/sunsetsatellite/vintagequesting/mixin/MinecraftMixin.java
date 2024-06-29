package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.window.GameWindow;
import net.minecraft.core.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.GuiQuestComplete;
import sunsetsatellite.vintagequesting.gui.GuiQuestbook;
import sunsetsatellite.vintagequesting.interfaces.IGuiQuestComplete;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;

@Mixin(value = Minecraft.class,remap = false)
public abstract class MinecraftMixin implements IGuiQuestComplete {

	@Unique
	private final Minecraft thisAs = (Minecraft) ((Object)this);

	@Shadow
	public GameSettings gameSettings;
	@Shadow
	public GuiScreen currentScreen;

	@Unique
	public GuiQuestComplete guiQuestComplete;

	@Shadow
	public abstract void displayGuiScreen(GuiScreen guiscreen);

	@Shadow
	public EntityPlayerSP thePlayer;
	@Unique
	private static int debounce = 0;

	@Inject(method = "<init>",at = @At("RETURN"))
	public void init(GameWindow gameWindow, CallbackInfo ci){
		guiQuestComplete = new GuiQuestComplete(thisAs);
	}

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiAchievement;updateAchievementWindow()V", shift = At.Shift.AFTER))
	public void run(CallbackInfo ci){
		guiQuestComplete.updateQuestCompleteWindow();
	}

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

	@Override
	public GuiQuestComplete getGuiQuestComplete() {
		return guiQuestComplete;
	}
}
