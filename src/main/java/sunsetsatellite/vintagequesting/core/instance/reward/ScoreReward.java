package sunsetsatellite.vintagequesting.core.instance.reward;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.data.reward.ScoreRewardData;

public class ScoreReward extends Reward {

	private final int amount;

	public ScoreReward(ScoreRewardData template) {
		super(template);
		this.amount = template.getScore();
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public void give(Player player) {
		if (!redeemed) {
			player.score += amount;
			redeemed = true;
		}
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		super.readFromNbt(nbt);
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		super.writeToNbt(nbt);
	}

}
