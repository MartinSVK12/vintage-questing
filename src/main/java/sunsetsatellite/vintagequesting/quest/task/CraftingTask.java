package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.vintagequesting.gui.generic.GuiString;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiCraftingTaskSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.task.CraftingTaskTemplate;

import java.util.List;

public class CraftingTask extends Task {

	protected ItemStack requirement;
	protected int progress = 0;
	protected boolean canConsume;
	protected boolean checksNbt;

	public CraftingTask(CraftingTaskTemplate template) {
		super(template);
		this.requirement = template.getStack();
		this.canConsume = template.canConsume();
		this.checksNbt = template.checksNbt();
	}

	public int addProgress(ItemStack stack) {
		if(stack == null) return -1;
		if(stack.isItemEqual(requirement)){
			if(checksNbt && !(stack.getData().equals(requirement.getData()))){
				return -1;
			}
			progress += stack.stackSize;
			return stack.stackSize;
		}
		return -1;
	}

	public void resetProgress() {
		progress = 0;
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
		return new CraftingTask((CraftingTaskTemplate) template);
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
		renderables.add(new GuiString(mc, (i+1)+". "+this.getTranslatedTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiCraftingTaskSlot(mc, width / 2 - 48, 24, this));
	}

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checksNbt;}
}
