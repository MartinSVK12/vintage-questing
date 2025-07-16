package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.vintagequesting.gui.generic.StringElement;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiRetrievalTaskSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.task.RetrievalTaskTemplate;

import java.util.List;

public class RetrievalTask extends Task {

	protected ItemStack requirement;
	protected int progress = 0;
	protected boolean canConsume;
	protected boolean checksNbt;
	protected boolean ignoreMeta;

	public RetrievalTask(RetrievalTaskTemplate template) {
		super(template);
		this.requirement = template.getStack();
		this.canConsume = template.canConsume();
		this.checksNbt = template.checksNbt();
		this.ignoreMeta = template.ignoresMeta();
	}

	public int setProgress(ItemStack stack, Player player) {
		if(stack == null) return -1;
		if(stack.isItemEqual(requirement) || (ignoreMeta && stack.itemID == requirement.itemID)){
			if(checksNbt && !(stack.getData().equals(requirement.getData()))){
				return -1;
			}
			if(canConsume){
				int amount = Math.min(stack.stackSize,requirement.stackSize - progress);
				int j = 0;
				for (int i = 0; i < amount; i++) {
					boolean b = player.inventory.consumeInventoryItem(stack.itemID);
					if(b) j++;
				}
				progress += j;
			} else {
				progress = stack.stackSize;
			}
			return stack.stackSize;
		}
		return -1;
	}

	public void resetProgress() {
		if(!canConsume) progress = 0;
	}

	public ItemStack getStack() {
		return requirement;
	}

	public int getProgress() {
		return progress;
	}

	@Override
	public boolean isCompleted() {
		return progress >= requirement.stackSize;
	}

	@Override
	public Task copy() {
		return new RetrievalTask((RetrievalTaskTemplate) template);
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		progress = nbt.getInteger("Progress");
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		nbt.putInt("Progress", progress);
	}

	@Override
	public void renderSlot(Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new StringElement(mc, (i+1)+". "+this.getTranslatedTypeName()+" | Consume: "+ (this.canConsume() ? TextFormatting.RED : TextFormatting.WHITE) +  this.canConsume(), 0xFFFFFFFF));
		renderables.add(new GuiRetrievalTaskSlot(mc, width / 2 - 48, 24, this));
	}

	@Override
	public void forceComplete() {
		progress = requirement.stackSize;
	}

	@Override
	public void reset() {
		progress = 0;
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checksNbt;}

	public boolean ignoresMeta() {return ignoreMeta;}
}
