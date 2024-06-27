package sunsetsatellite.vintagequesting.quest;

import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

public abstract class Task {

	protected boolean canConsume;
	protected boolean checkNbt;
	protected String typeName;
	protected final TaskTemplate template;

	public Task(TaskTemplate template) {
		this.template = template;
		this.typeName = template.getTypeName();
		this.checkNbt = template.checksNbt();
		this.canConsume = template.canConsume();
	}

	public abstract boolean isCompleted();

	public String getTypeName() {
		return typeName;
	}

	public String getTranslatedTypeName(){
		return I18n.getInstance().translateNameKey(typeName);
	}

	public abstract Task copy();

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checkNbt;}
}
