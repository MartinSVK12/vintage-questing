package sunsetsatellite.vintagequesting.util;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.mojang.nbt.tags.StringTag;
import com.mojang.nbt.tags.Tag;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.ChapterData;

import java.util.*;

public class QuestTeam {

	public String name;
	public UUID owner;
	public List<UUID> members = new ArrayList<>();
	public Map<String, Chapter> chapters = new HashMap<>();
	public List<UUID> invites = new ArrayList<>();

	public QuestTeam(String name, UUID owner) {
		this.name = name;
		this.owner = owner;
		members.add(owner);
		reset();
		for (Chapter chapter : getChapters()) {
			chapter.questList.forEach(quest -> quest.setupPrerequisites(this));
		}
	}

	public QuestTeam(CompoundTag tag) {
		readFromNbt(tag);
		readChapters(tag);
		for (Chapter chapter : getChapters()) {
			chapter.questList.forEach(quest -> quest.setupPrerequisites(this));
		}
	}

	public void reset(){
		chapters.clear();
		for (ChapterData chapter : VintageQuesting.CHAPTERS) {
			chapters.put(chapter.id, new Chapter(chapter, this));
		}
		for (Chapter chapter : getChapters()) {
			chapter.questList.forEach(quest -> quest.setupPrerequisites(this));
		}
	}

	public CompoundTag readFromNbt(CompoundTag tag) {
		name = tag.getString("Name");
		owner = UUID.fromString(tag.getString("Owner"));
		ListTag membersList = tag.getList("Members");
		for (Tag<?> t : membersList) {
			if(t instanceof StringTag memberTag){
				members.add(UUID.fromString(memberTag.getValue()));
			}
		}
		return tag;
	}

	public void readChapters(CompoundTag tag){
		reset();
		CompoundTag chapters = tag.getCompoundOrDefault("Chapters", null);
		if (chapters != null) {
			Map<String, Tag<?>> chapterMap = chapters.getValue();
			for (Map.Entry<String, Tag<?>> entry : chapterMap.entrySet()) {
				String id = entry.getKey();
				Tag<?> mapTag = entry.getValue();
				CompoundTag chapterTag = ((CompoundTag) mapTag);
				Chapter chapter = this.chapters.get(id);
				if (chapter != null) {
					for (Quest quest : chapter.getQuests()) {
						CompoundTag questTag = chapterTag.getCompoundOrDefault(quest.data.getId(), null);
						if (questTag != null) {
							/*if(EnvironmentHelper.isMultiplayerServer()){
								questPackets.add(new NetworkMessageQuestSync(chapter.getId(), questTag));
							}*/
							CompoundTag tasks = questTag.getCompound("Tasks");
							CompoundTag rewards = questTag.getCompound("Rewards");
							quest.readFromNbt(questTag);
							for (Reward reward : quest.getRewards()) {
								CompoundTag rewardTag = rewards.getCompoundOrDefault(reward.data.getId(), null);
								if (rewardTag != null) {
									reward.readFromNbt(rewardTag);
								} else {
									VintageQuesting.LOGGER.error("No reward with id: {}", reward.data.getId());
								}
							}
							for (Task task : quest.getTasks()) {
								CompoundTag taskTag = tasks.getCompoundOrDefault(task.data.getId(), null);
								if (taskTag != null) {
									task.readFromNbt(taskTag);
								} else {
									VintageQuesting.LOGGER.error("No task with id: {}", task.data.getId());
								}
							}
						} else {
							VintageQuesting.LOGGER.error("No quest with id: {}", quest.data.getId());
						}
					}
				} else {
					VintageQuesting.LOGGER.error("No chapter with id: {}", id);
				}
			}
		}
	}

	public CompoundTag writeToNbt(CompoundTag tag) {
		tag.putString("Name", name);
		tag.putString("Owner", owner.toString());
		ListTag membersTag = Catalyst.listTagOf(members.stream().map(Objects::toString).toList());
		tag.put("Members", membersTag);
		return tag;
	}

	public void writeChapters(CompoundTag tag) {
		CompoundTag chaptersTag = new CompoundTag();
		for (Chapter chapter : getChapters()) {
			CompoundTag chapterTag = new CompoundTag();
			for (Quest quest : chapter.getQuests()) {
				CompoundTag questTag = new CompoundTag();
				CompoundTag tasksTag = new CompoundTag();
				CompoundTag rewardsTag = new CompoundTag();
				for (Task task : quest.getTasks()) {
					CompoundTag taskTag = new CompoundTag();
					task.writeToNbt(taskTag);
					tasksTag.putCompound(task.data.getId(), taskTag);
				}
				for (Reward reward : quest.getRewards()) {
					CompoundTag rewardTag = new CompoundTag();
					reward.writeToNbt(rewardTag);
					rewardsTag.putCompound(reward.data.getId(), rewardTag);
				}
				questTag.putCompound("Tasks", tasksTag);
				questTag.putCompound("Rewards", rewardsTag);
				quest.writeToNbt(questTag);
				chapterTag.putCompound(quest.data.getId(), questTag);
			}
			chaptersTag.putCompound(chapter.getId(), chapterTag);
		}
		tag.put("Chapters", chaptersTag);
	}

	public Collection<Chapter> getChapters() {
		return chapters.values();
	}
}
