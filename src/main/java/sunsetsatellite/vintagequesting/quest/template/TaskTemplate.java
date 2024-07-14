package sunsetsatellite.vintagequesting.quest.template;

import net.minecraft.core.lang.I18n;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.quest.Task;

public abstract class TaskTemplate {

	protected final String typeName;
	protected final String id;

	public TaskTemplate(String id, String typeName) {
		this.typeName = typeName;
		this.id = id;
		VintageQuesting.TASKS.register(id,this);
	}

	public String getTypeName() {
		return typeName;
	}

	public String getTranslatedTypeName(){
		return I18n.getInstance().translateNameKey(typeName);
	}

	public abstract TaskTemplate copy();

	public abstract Task getInstance();

	public abstract Task getInstanceUnique();

	public String getId() {
		return id;
	}

	public abstract void reset();
}
