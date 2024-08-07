package sunsetsatellite.vintagequesting.quest.reward;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.vintagequesting.gui.generic.GuiString;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.template.reward.ScoreRewardTemplate;

import java.util.List;

public class ScoreReward extends Reward {

	private final int amount;

	public ScoreReward(ScoreRewardTemplate template) {
		super(template);
		this.amount = template.getScore();
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public void give(EntityPlayer player) {
		if(!redeemed){
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

	@Override
	public void renderSlot(Minecraft mc, List<IRenderable> renderables, int width) {
		renderables.add(new GuiString(mc,"Item Reward:",0xFFFFFFFF));
		renderables.add(new GuiString(mc,"Score +"+getAmount(),0xFF00FF00));
		//renderables.add(new GuiItemRewardSlot(mc,width / 2 - 38,24, this));
	}
}
