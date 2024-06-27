package sunsetsatellite.vintagequesting.quest;

import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;

public abstract class Reward {

	public Reward(RewardTemplate template){}

	public boolean redeemed;

	public abstract void give(EntityPlayer player);

	public boolean isRedeemed(){
		return redeemed;
	}
}
