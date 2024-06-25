package sunsetsatellite.vintagequesting.quest;

import net.minecraft.core.lang.I18n;

public abstract class Task {

	protected boolean canConsume;
	protected boolean checkNBT;
	protected String typeName;

	public Task(String typeName) {
		this.typeName = typeName;
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
}
