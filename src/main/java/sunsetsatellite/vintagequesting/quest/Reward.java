package sunsetsatellite.vintagequesting.quest;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

import java.util.List;

public abstract class Reward {

	protected final RewardTemplate template;

	public Reward(RewardTemplate template) {
		this.template = template;
	}

	public boolean redeemed;

	public abstract void give(Player player);

	public boolean isRedeemed() {
		return redeemed;
	}

	public RewardTemplate getTemplate() {
		return template;
	}

	public void readFromNbt(CompoundTag nbt) {
		this.redeemed = nbt.getBoolean("Redeemed");
	}

	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Redeemed", redeemed);
	}

	public abstract void renderSlot(Minecraft mc, List<IRenderable> renderables, int width);
}
