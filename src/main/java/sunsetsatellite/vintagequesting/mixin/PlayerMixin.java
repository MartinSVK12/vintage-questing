package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin implements IHasQuests { }
