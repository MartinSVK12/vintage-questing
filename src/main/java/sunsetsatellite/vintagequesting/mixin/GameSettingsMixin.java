package sunsetsatellite.vintagequesting.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import sunsetsatellite.vintagequesting.interfaces.IKeybinds;

@Environment(EnvType.CLIENT)
@Mixin(
        value = GameSettings.class,
        remap = false
)
public class GameSettingsMixin
    implements IKeybinds
{
    @Unique
    public KeyBinding keyOpenQuestbook = new KeyBinding("key.vintagequesting.openQuestbook").bind(InputDevice.keyboard, Keyboard.KEY_GRAVE);


	@Override
	public KeyBinding vintage_questing$getKeyOpenQuestbook() {
		return keyOpenQuestbook;
	}
}
