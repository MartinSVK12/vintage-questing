package sunsetsatellite.vintagequesting.quest.reward;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.gui.slot.reward.GuiItemRewardSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.template.reward.ItemRewardTemplate;

import java.util.List;

public class ItemReward extends Reward {

	private final ItemStack item;

	public ItemReward(ItemRewardTemplate template) {
		super(template);
		this.item = template.getStack();
	}

	public ItemStack getStack() {
		return item.copy();
	}

	@Override
	public void give(EntityPlayer player) {
		if(!redeemed){
			ItemStack stack = item.copy();
			player.inventory.insertItem(stack,true);
			if(stack.stackSize > 0){
				player.dropPlayerItem(stack);
			}
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
		renderables.add(new GuiItemRewardSlot(mc,width / 2 - 38,24, this));
	}
}
