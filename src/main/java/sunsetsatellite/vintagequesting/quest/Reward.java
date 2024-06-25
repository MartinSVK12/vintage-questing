package sunsetsatellite.vintagequesting.quest;

import net.minecraft.core.entity.player.EntityPlayer;

public abstract class Reward {

	public boolean redeemed;

	public abstract void give(EntityPlayer player);

	public boolean isRedeemed(){
		return redeemed;
	}
}
