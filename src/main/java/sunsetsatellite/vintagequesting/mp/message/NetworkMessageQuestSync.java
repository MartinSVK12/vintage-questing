package sunsetsatellite.vintagequesting.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageQuestSync implements NetworkMessage {

	public String chapterId;
	public CompoundTag questTag;

	public NetworkMessageQuestSync() {
	}

	public NetworkMessageQuestSync(String chapterId, CompoundTag questTag) {
		this.chapterId = chapterId;
		this.questTag = questTag;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeString(chapterId);
		packet.writeCompoundTag(questTag);
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		chapterId = packet.readString();
		questTag = packet.readCompoundTag();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void handleClientEnv(NetworkContext context) {
		Chapter chapter = ((IHasQuests) context.player).getQuestTeam().chapters.get(chapterId);
		if(chapter == null) {
			VintageQuesting.LOGGER.error("Error while receiving quest data: No chapter with id: {}!", chapterId);
			return;
		}
		if (questTag == null || questTag.getValues().isEmpty()) {
			VintageQuesting.LOGGER.error("Error while receiving quest data: Received empty tag!");
			return;
		}
		Quest quest = chapter.getQuest(VintageQuesting.QUESTS.getItem(questTag.getTagName()));
		if(quest == null){
			VintageQuesting.LOGGER.error("Error while receiving quest data: No quest with id: {}", questTag.getTagName());
			return;
		}
		CompoundTag tasks = questTag.getCompound("Tasks");
		CompoundTag rewards = questTag.getCompound("Rewards");
		quest.readFromNbt(questTag);
		for (Reward reward : quest.getRewards()) {
			CompoundTag rewardTag = rewards.getCompoundOrDefault(reward.data.getId(), null);
			if (rewardTag == null) {
				VintageQuesting.LOGGER.error("Error while receiving quest data: No reward with id: {}", reward.data.getId());
			} else {
				reward.readFromNbt(rewardTag);
			}
		}
		for (Task task : quest.getTasks()) {
			CompoundTag taskTag = tasks.getCompoundOrDefault(task.data.getId(), null);
			if (taskTag == null) {
				VintageQuesting.LOGGER.error("Error while receiving quest data: No task with id: {}", task.data.getId());
			} else {
				task.readFromNbt(taskTag);
			}
		}
	}
}
