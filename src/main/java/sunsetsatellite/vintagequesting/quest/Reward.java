package sunsetsatellite.vintagequesting.quest;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

public abstract class Reward {

	protected final RewardTemplate template;

	public Reward(RewardTemplate template){
		this.template = template;
	}

	public boolean redeemed;

	public abstract void give(EntityPlayer player);

	public boolean isRedeemed(){
		return redeemed;
	}

	public RewardTemplate getTemplate() {
		return template;
	}

	public void readFromNbt(CompoundTag nbt){
		this.redeemed = nbt.getBoolean("Redeemed");
	};

	public void writeToNbt(CompoundTag nbt){
		nbt.putBoolean("Redeemed", redeemed);
	};
}
