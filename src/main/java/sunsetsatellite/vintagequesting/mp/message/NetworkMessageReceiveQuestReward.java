package sunsetsatellite.vintagequesting.mp.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.ClickTask;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.Objects;

public class NetworkMessageReceiveQuestReward implements NetworkMessage {

	public String chapterId;
	public String questId;

	public NetworkMessageReceiveQuestReward() {
	}

	public NetworkMessageReceiveQuestReward(String chapterId, String questId) {
		this.chapterId = chapterId;
		this.questId = questId;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeString(chapterId);
		packet.writeString(questId);
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		chapterId = packet.readString();
		questId = packet.readString();
	}

	@Environment(EnvType.SERVER)
	@Override
	public void handleServerEnv(NetworkContext context) {
		Chapter chapter = ((IHasQuests) context.player).getQuestTeam().chapters.get(chapterId);
		if(chapter == null) {
			VintageQuesting.LOGGER.error("Error while receiving quest data: No chapter with id: {}!", chapterId);
			return;
		}
		Quest quest = chapter.getQuest(VintageQuesting.QUESTS.getItem(questId));
		if(quest == null){
			VintageQuesting.LOGGER.error("Error while receiving quest data: No quest with id: {}", questId);
			return;
		}
		for (Reward reward : quest.rewards) {
			reward.give(context.player);
		}
	}
}
