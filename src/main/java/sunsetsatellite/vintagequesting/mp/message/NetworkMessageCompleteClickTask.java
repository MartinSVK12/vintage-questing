package sunsetsatellite.vintagequesting.mp.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.ClickTask;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.Objects;

public class NetworkMessageCompleteClickTask implements NetworkMessage {

	public String chapterId;
	public String questId;
	public String taskId;

	public NetworkMessageCompleteClickTask() {
	}

	public NetworkMessageCompleteClickTask(String chapterId, String questId, String taskId) {
		this.chapterId = chapterId;
		this.questId = questId;
		this.taskId = taskId;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeString(chapterId);
		packet.writeString(questId);
		packet.writeString(taskId);
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		chapterId = packet.readString();
		questId = packet.readString();
		taskId = packet.readString();
	}

	@Environment(EnvType.SERVER)
	@Override
	public void handleServerEnv(NetworkContext context) {
		Chapter chapter = VintageQuesting.CHAPTERS.getItem(chapterId);
		if(chapter == null) {
			VintageQuesting.LOGGER.error("Error while trying to complete click task: No chapter with id: {}!", chapterId);
			return;
		}
		Quest quest = chapter.getQuest(VintageQuesting.QUESTS.getItem(questId));
		if(quest == null){
			VintageQuesting.LOGGER.error("Error while trying to complete click task: No quest with id '{}' in chapter with id '{}'!", questId, chapterId);
			return;
		}
		boolean foundTask = false;
		for (Task task : quest.tasks) {
			if(Objects.equals(task.data.getId(), taskId)){
				if(task instanceof ClickTask clickTask){
					clickTask.click();
					foundTask = true;
					break;
				}
			}
		}
		if(!foundTask){
			VintageQuesting.LOGGER.error("Error while trying to complete click task: No task with id '{}' inside quest with id '{}' in chapter with id '{}'!", taskId, questId, chapterId);
		}
	}
}
