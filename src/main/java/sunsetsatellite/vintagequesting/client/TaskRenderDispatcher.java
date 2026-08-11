package sunsetsatellite.vintagequesting.client;

import net.minecraft.client.util.dispatch.Dispatcher;
import sunsetsatellite.vintagequesting.client.render.RewardRenderer;
import sunsetsatellite.vintagequesting.client.render.TaskRenderer;
import sunsetsatellite.vintagequesting.client.render.task.*;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.*;

public class TaskRenderDispatcher extends Dispatcher<Class<? extends Task>, TaskRenderer<? extends Task>> {

	private static TaskRenderDispatcher instance = new TaskRenderDispatcher();

	private TaskRenderDispatcher() {
		reload();
	}

	public static TaskRenderDispatcher getInstance() {
		return instance;
	}

	public void reload() {
		dispatches.clear();
		addDispatch(ClickTask.class, new ClickTaskRenderer());
		addDispatch(CraftingTask.class, new CraftingTaskRenderer());
		addDispatch(KillTask.class, new KillTaskRenderer());
		addDispatch(RetrievalTask.class, new RetrievalTaskRenderer());
		addDispatch(VisitDimensionTask.class, new VisitDimensionTaskRenderer());
	}

	@Override
	protected TaskRenderer<?> getDefault() {
		return null;
	}

	public TaskRenderer<Task> getDispatch(Class<? extends Task> aClass) {
		return (TaskRenderer<Task>) super.getDispatch(aClass);
	}
}
