package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.vintagequesting.gui.generic.GuiString;
import sunsetsatellite.vintagequesting.gui.slot.task.GuiVisitDImensionTaskSlot;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.task.VisitDimensionTaskTemplate;

import java.util.List;

public class VisitDimensionTask extends Task {

    protected boolean visited = false;
	protected Dimension dimension;

	public VisitDimensionTask(VisitDimensionTaskTemplate template) {
		super(template);
		dimension = template.getDimension();
	}

	public void check(EntityPlayer player) {
		if(!visited) visited = player.dimension == dimension.id;
    }

	@Override
	public boolean isCompleted() {
		return visited;
	}

	public Dimension getDimension() {
		return dimension;
	}

	@Override
	public Task copy() {
		return new VisitDimensionTask((VisitDimensionTaskTemplate) template);
	}

	@Override
	public void readFromNbt(CompoundTag nbt) {
		visited = nbt.getBoolean("Visited");
	}

	@Override
	public void writeToNbt(CompoundTag nbt) {
		nbt.putBoolean("Visited", visited);
	}

	@Override
	public void renderSlot(Minecraft mc, List<IRenderable> renderables, int i, int width) {
		renderables.add(new GuiString(mc, (i+1)+". "+this.getTranslatedTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiVisitDImensionTaskSlot(mc, width / 2 - 48, 20, this));
	}

}
