package sunsetsatellite.vintagequesting.mixin;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;

import java.util.Map;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin {

	@Unique
	private final Player thisAs = (Player) ((Object) this);

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(World world, CallbackInfo ci) {
		VintageQuesting.LOGGER.info("Initializing quests...");
		resetAll();
	}

	@Unique
	public void loadData(CompoundTag tag) {
		resetAll();

		CompoundTag chapters = tag.getCompoundOrDefault("QuestingChapters", null);
		if (chapters != null) {
			Map<String, Tag<?>> chapterMap = chapters.getValue();
			if (chapters.getValues().isEmpty()) {
				//VintageQuesting.LOGGER.warn("No data. Loading defaults...");
				for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
					//questGroup.quests.addAll(chapter.getQuests());
				}
				return;
			}
			for (Map.Entry<String, Tag<?>> entry : chapterMap.entrySet()) {
				String id = entry.getKey();
				Tag<?> mapTag = entry.getValue();
				CompoundTag chapterTag = ((CompoundTag) mapTag);
				QuestChapterPage chapterPage = VintageQuesting.CHAPTERS.getItem(id);
				if (chapterPage != null) {
					for (Quest quest : chapterPage.getQuests()) {
						CompoundTag questTag = chapterTag.getCompoundOrDefault(quest.getTemplate().getId(), null);
						if (questTag != null) {
							CompoundTag tasks = questTag.getCompound("Tasks");
							CompoundTag rewards = questTag.getCompound("Rewards");
							quest.readFromNbt(questTag);
							for (Reward reward : quest.getRewards()) {
								CompoundTag rewardTag = rewards.getCompoundOrDefault(reward.getTemplate().getId(), null);
								if (rewardTag != null) {
									reward.readFromNbt(rewardTag);
								} else {
									VintageQuesting.LOGGER.error("No reward with id: " + reward.getTemplate().getId());
								}
							}
							for (Task task : quest.getTasks()) {
								CompoundTag taskTag = tasks.getCompoundOrDefault(task.getTemplate().getId(), null);
								if (taskTag != null) {
									task.readFromNbt(taskTag);
								} else {
									VintageQuesting.LOGGER.error("No task with id: " + task.getTemplate().getId());
								}
							}
						} else {
							VintageQuesting.LOGGER.error("No quest with id: " + quest.getTemplate().getId());
						}
					}
					VintageQuesting.LOGGER.info("Loaded saved data for chapter with id: " + id);
				} else {
					VintageQuesting.LOGGER.error("No chapter with id: " + id);
				}
			}
		} else {
			//VintageQuesting.LOGGER.warn("No data. Loading defaults...");
			for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
				//questGroup.quests.addAll(chapter.getQuests());
			}
		}
	}

	@Unique
	public void resetAll() {
		for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
			chapter.reset();
			for (QuestTemplate template : chapter.getQuestTemplates()) {
				for (RewardTemplate reward : template.getRewards()) {
					reward.reset();
				}
				for (TaskTemplate task : template.getTasks()) {
					task.reset();
				}
			}
		}
	}

	@Unique
	public void resetChapter(String id) {
		QuestChapterPage chapter = VintageQuesting.CHAPTERS.getItem(id);
		if (chapter == null) return;
		chapter.reset();
		for (QuestTemplate template : chapter.getQuestTemplates()) {
			for (RewardTemplate reward : template.getRewards()) {
				reward.reset();
			}
			for (TaskTemplate task : template.getTasks()) {
				task.reset();
			}
		}
	}

	@Unique
	public void resetQuest(String id) {
		QuestTemplate quest = VintageQuesting.QUESTS.getItem(id);
		if (quest == null) return;
		for (RewardTemplate reward : quest.getRewards()) {
			reward.reset();
		}
		for (TaskTemplate task : quest.getTasks()) {
			task.reset();
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		CompoundTag chaptersTag = new CompoundTag();
		for (QuestChapterPage chapter : VintageQuesting.CHAPTERS) {
			CompoundTag chapterTag = new CompoundTag();
			for (Quest quest : chapter.getQuests()) {
				CompoundTag questTag = new CompoundTag();
				CompoundTag tasksTag = new CompoundTag();
				CompoundTag rewardsTag = new CompoundTag();
				questTag.putCompound("Tasks", tasksTag);
				questTag.putCompound("Rewards", rewardsTag);
				quest.writeToNbt(questTag);
				for (Task task : quest.getTasks()) {
					CompoundTag taskTag = new CompoundTag();
					task.writeToNbt(taskTag);
					tasksTag.putCompound(task.getTemplate().getId(), taskTag);
				}
				for (Reward reward : quest.getRewards()) {
					CompoundTag rewardTag = new CompoundTag();
					reward.writeToNbt(rewardTag);
					rewardsTag.putCompound(reward.getTemplate().getId(), rewardTag);
				}
				chapterTag.putCompound(quest.getTemplate().getId(), questTag);
			}
			chaptersTag.putCompound(chapter.getId(), chapterTag);
		}
		tag.putCompound("QuestingChapters", chaptersTag);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		loadData(tag);
	}
}
