package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.quest.Task;

public abstract class TaskTemplate {

	protected boolean canConsume;
	protected boolean checkNbt;
	protected String typeName;

	public TaskTemplate(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getTranslatedTypeName(){
		return I18n.getInstance().translateNameKey(typeName);
	}

	public abstract TaskTemplate copy();

	public boolean canConsume() {
		return canConsume;
	}

	public boolean checksNbt() {return checkNbt;}

	public abstract Task getInstance();
}
