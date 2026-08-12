package sunsetsatellite.vintagequesting.mp.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.instance.reward.ChoiceItemReward;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.Objects;

public class NetworkMessageChooseItemReward implements NetworkMessage {

	public String chapterId;
	public String questId;
	public String rewardId;
	public int choice;

	public NetworkMessageChooseItemReward() {
	}

	public NetworkMessageChooseItemReward(String chapterId, String questId, String rewardId, int choice) {
		this.chapterId = chapterId;
		this.questId = questId;
		this.rewardId = rewardId;
		this.choice = choice;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeString(chapterId);
		packet.writeString(questId);
		packet.writeString(rewardId);
		packet.writeInt(choice);
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		chapterId = packet.readString();
		questId = packet.readString();
		rewardId = packet.readString();
		choice = packet.readInt();
	}

	@Environment(EnvType.SERVER)
	@Override
	public void handleServerEnv(NetworkContext context) {
		QuestTeam team = ((IHasQuests) context.player).getQuestTeam();
		Chapter chapter = team.chapters.get(chapterId);
		if(chapter == null) {
			VintageQuesting.LOGGER.error("Error while trying to choose item reward: No chapter with id: {}!", chapterId);
			return;
		}
		Quest quest = chapter.getQuest(VintageQuesting.QUESTS.getItem(questId));
		if(quest == null){
			VintageQuesting.LOGGER.error("Error while trying to choose item reward: No quest with id '{}' in chapter with id '{}'!", questId, chapterId);
			return;
		}
		boolean foundTask = false;
		for (Reward reward : quest.rewards) {
			if(Objects.equals(reward.data.getId(), rewardId)){
				if(reward instanceof ChoiceItemReward choiceItemReward){
					choiceItemReward.choose(choice);
					//clickTask.click();
					//foundTask = true;
					((IHasQuests) context.player).synchronizeQuests();
					break;
				}
			}
		}
		if(!foundTask){
			VintageQuesting.LOGGER.error("Error while trying to choose item reward: No reward with id '{}' inside quest with id '{}' in chapter with id '{}'!", rewardId, questId, chapterId);
		}
	}
}
