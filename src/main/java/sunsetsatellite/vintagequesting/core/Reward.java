package sunsetsatellite.vintagequesting.core;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.core.data.RewardData;

public abstract class Reward {

	public final RewardData data;
	public boolean redeemed;

	public Reward(RewardData data) {
		this.data = data;
	}

	public void readFromNbt(CompoundTag nbt) {
		this.redeemed = nbt.getBoolean("Redeemed");
	}

	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Redeemed", redeemed);
	}

	public boolean isRedeemed() {
		return redeemed;
	}

	public abstract void give(Player player);
}
