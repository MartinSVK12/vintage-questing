package sunsetsatellite.vintagequesting.quest.task;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.Dimension;
import sunsetsatellite.vintagequesting.gui.generic.StringElement;
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

	public void check(Player player) {
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
		renderables.add(new StringElement(mc, (i+1)+". "+this.getTranslatedTypeName(), 0xFFFFFFFF));
		renderables.add(new GuiVisitDImensionTaskSlot(mc, width / 2 - 48, 20, this));
	}

	@Override
	public void forceComplete() {
		visited = true;
	}

	@Override
	public void reset() {
		visited = false;
	}

}
