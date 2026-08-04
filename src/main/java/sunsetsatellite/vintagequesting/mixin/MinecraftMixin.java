package sunsetsatellite.vintagequesting.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.Screen;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.VintageQuestingClient;
import sunsetsatellite.vintagequesting.gui.ScreenQuestbook;

@Environment(EnvType.CLIENT)
@Mixin(value = Minecraft.class, remap = false)
public abstract class MinecraftMixin {

	@Unique
	private final Minecraft thisAs = (Minecraft) ((Object) this);

	@Shadow
	public PlayerLocal thePlayer;
	@Shadow
	public Screen currentScreen;

	@Shadow
	public abstract void displayScreen(Screen par1);

	@Unique
	private static int debounce = 0;

	@Inject(
		method = "runTick",
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER)
	)
	public void handleKeyboard(CallbackInfo ci) {
		if (debounce > 0) debounce--;
		boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
		boolean control = (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157));
		if (debounce <= 0) {
			if (VintageQuestingClient.keyOpenQuestbook.isPressed() && currentScreen == null) {
				displayScreen(new ScreenQuestbook(null, VintageQuesting.CHAPTERS.getItemByNumericId(0)));
				debounce = 10;
			}
		}
	}

	/*@Inject(
		method = "respawn",
		at = @At("HEAD")
	)
	public void saveQuestsOnRespawn(boolean flag, int i, CallbackInfo ci, @Share("questGroup") LocalRef<QuestGroup> questGroup) {
		questGroup.set(((IHasQuests) thePlayer).getQuestGroup());
	}

	@Inject(
		method = "respawn",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/controller/PlayerController;adjustPlayer(Lnet/minecraft/core/entity/player/Player;)V",shift = At.Shift.AFTER)
	)
	public void restoreQuestsOnRespawn(boolean flag, int i, CallbackInfo ci, @Share("questGroup") LocalRef<QuestGroup> questGroup) {
		((IHasQuests) thePlayer).setQuestGroup(questGroup.get());
	}*/
}
