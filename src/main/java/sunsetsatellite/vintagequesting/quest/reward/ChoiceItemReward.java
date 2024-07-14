package sunsetsatellite.vintagequesting.quest.reward;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.gui.generic.GuiString;
import sunsetsatellite.vintagequesting.gui.slot.reward.GuiChoiceItemRewardSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.template.reward.ChoiceItemRewardTemplate;

import java.util.List;

public class ChoiceItemReward extends Reward {

	private ItemStack chosen;
	private final List<ItemStack> stacks;

	public ChoiceItemReward(ChoiceItemRewardTemplate template) {
		super(template);
		this.stacks = template.getStacks();
	}

	public ItemStack getChosenStack() {
		if(chosen == null) return null;
		return chosen.copy();
	}

	public ItemStack getOption(int index){
		return stacks.get(index);
	}

	public void choose(int index){
		chosen = stacks.get(index);
	}

	@Override
	public void give(EntityPlayer player) {
		if(chosen == null) return;
		if(!redeemed){
			ItemStack stack = chosen.copy();
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
		renderables.add(new GuiString(mc,"Choice Item Reward:",0xFFFFFFFF));
		for (int i = 0; i < stacks.size(); i++) {
			renderables.add(new GuiChoiceItemRewardSlot(mc,width / 2 - 38,24, this,i));
		}

	}
}
