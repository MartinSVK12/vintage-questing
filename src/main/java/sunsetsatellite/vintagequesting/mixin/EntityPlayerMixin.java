package sunsetsatellite.vintagequesting.mixin;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.Reward;
import sunsetsatellite.vintagequesting.quest.Task;
import sunsetsatellite.vintagequesting.quest.task.KillTask;
import sunsetsatellite.vintagequesting.quest.template.ChapterTemplate;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;
import sunsetsatellite.vintagequesting.quest.template.RewardTemplate;
import sunsetsatellite.vintagequesting.quest.template.TaskTemplate;
import sunsetsatellite.vintagequesting.util.QuestGroup;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = EntityPlayer.class,remap = false)
public class EntityPlayerMixin implements IHasQuests {
	@Unique
	private final EntityPlayer thisAs = (EntityPlayer) ((Object)this);

	@Unique
	public Chapter currentChapter;

	@Unique
	public QuestGroup questGroup = new QuestGroup();

	@Override
	public QuestGroup getQuestGroup() {
		return questGroup;
	}

	@Override
	public void setQuestGroup(QuestGroup group) {
		questGroup = group;
	}

	@Override
	public void setCurrentChapter(Chapter chapter) {
		currentChapter = chapter;
	}

	@Override
	public Chapter getCurrentChapter() {
		return currentChapter;
	}

	@Override
	public void loadData(CompoundTag tag) {
		VintageQuesting.playerData = tag;
		questGroup.chapters.clear();
		resetAll();
		setCurrentChapter(null);
		CompoundTag chapters = tag.getCompoundOrDefault("QuestingChapters", null);
		if(chapters != null) {
			Map<String, Tag<?>> chapterMap = chapters.getValue();
			if(chapters.getValues().isEmpty()){
				VintageQuesting.LOGGER.warn("No data. Loading defaults...");
				for (ChapterTemplate chapter : VintageQuesting.CHAPTERS) {
					questGroup.chapters.add(chapter.getInstance());
				}
				return;
			}
			chapterMap.forEach((String id, Tag<?> mapTag) -> {
				CompoundTag chapterTag = ((CompoundTag) mapTag);
				ChapterTemplate chapterTemplate = VintageQuesting.CHAPTERS.getItem(id);
				if(chapterTemplate != null) {
					Chapter chapter = chapterTemplate.getInstance();
					for (Quest quest : chapter.getQuests()) {
						CompoundTag questTag = chapterTag.getCompoundOrDefault(quest.getTemplate().getId(),null);
						if(questTag != null) {
							CompoundTag tasks = questTag.getCompound("Tasks");
							CompoundTag rewards = questTag.getCompound("Rewards");
							quest.readFromNbt(questTag);
							for (Reward reward : quest.getRewards()) {
								CompoundTag rewardTag = rewards.getCompoundOrDefault(reward.getTemplate().getId(),null);
								if(rewardTag != null){
									reward.readFromNbt(rewardTag);
								} else {
									VintageQuesting.LOGGER.error("No reward with id: "+reward.getTemplate().getId());
								}
							}
							for (Task task : quest.getTasks()) {
								CompoundTag taskTag = tasks.getCompoundOrDefault(task.getTemplate().getId(),null);
								if(taskTag != null){
									task.readFromNbt(taskTag);
								} else {
									VintageQuesting.LOGGER.error("No task with id: "+task.getTemplate().getId());
								}
							}
						} else {
							VintageQuesting.LOGGER.error("No quest with id: "+quest.getTemplate().getId());
						}
					}
					questGroup.chapters.add(chapter);
					VintageQuesting.LOGGER.info("Loaded saved data for chapter with id: "+id);
				} else {
					VintageQuesting.LOGGER.error("No chapter with id: "+id);
				}
			});
			VintageQuesting.CHAPTERS.forEach((C)->{
				if(questGroup.chapters.stream().noneMatch((QC)-> Objects.equals(QC.getTemplate().getId(), C.getId()))){
					VintageQuesting.LOGGER.warn("Adding missing chapter with id: "+C.getId());
					questGroup.chapters.add(C.getInstance());
				}
			});
		} else {
			VintageQuesting.LOGGER.warn("No data. Loading defaults...");
			for (ChapterTemplate chapter : VintageQuesting.CHAPTERS) {
				questGroup.chapters.add(chapter.getInstance());
			}
		}
	}

	@Override
	public void resetAll() {
		for (ChapterTemplate chapter : VintageQuesting.CHAPTERS) {
			chapter.reset();
			for (QuestTemplate quest : chapter.getQuests()) {
				quest.reset();
				for (RewardTemplate reward : quest.getRewards()) {
					reward.reset();
				}
				for (TaskTemplate task : quest.getTasks()) {
					task.reset();
				}
			}
		}
	}

	@Override
	public void resetChapter(String id) {
		ChapterTemplate chapter = VintageQuesting.CHAPTERS.getItem(id);
		if(chapter == null) return;
		chapter.reset();
		for (QuestTemplate quest : chapter.getQuests()) {
			quest.reset();
			for (RewardTemplate reward : quest.getRewards()) {
				reward.reset();
			}
			for (TaskTemplate task : quest.getTasks()) {
				task.reset();
			}
		}
	}

	@Override
	public void resetQuest(String id) {
		QuestTemplate quest = VintageQuesting.QUESTS.getItem(id);
		if(quest == null) return;
		quest.reset();
		for (RewardTemplate reward : quest.getRewards()) {
			reward.reset();
		}
		for (TaskTemplate task : quest.getTasks()) {
			task.reset();
		}
	}

	@Inject(method = "awardKillScore", at = @At("HEAD"))
	public void awardKillScore(Entity entity, int i, CallbackInfo ci) {
		for (Chapter chapter : questGroup.chapters) {
			for (Quest quest : chapter.getQuests()) {
				for (Task task : quest.getTasks()) {
					if(task instanceof KillTask){
						((KillTask) task).addProgress(entity.getClass());
					}
				}
				quest.isCompleted();
			}
		}
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(World world, CallbackInfo ci){
		VintageQuesting.LOGGER.info("Initializing quests...");
		questGroup.chapters.clear();
		resetAll();
		for (ChapterTemplate chapter : VintageQuesting.CHAPTERS) {
			questGroup.chapters.add(chapter.getInstance());
		}
	}

	@Inject(method = "addAdditionalSaveData",at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		CompoundTag chaptersTag = new CompoundTag();
		if(questGroup.chapters.isEmpty()) return;
		for (Chapter chapter : questGroup.chapters) {
			CompoundTag chapterTag = new CompoundTag();
			for (Quest quest : chapter.getQuests()) {
				CompoundTag questTag = new CompoundTag();
				CompoundTag tasksTag = new CompoundTag();
				CompoundTag rewardsTag = new CompoundTag();
				questTag.putCompound("Tasks",tasksTag);
				questTag.putCompound("Rewards",rewardsTag);
				quest.writeToNbt(questTag);
				for (Task task : quest.getTasks()) {
					CompoundTag taskTag = new CompoundTag();
					task.writeToNbt(taskTag);
					tasksTag.putCompound(task.getTemplate().getId(),taskTag);
				}
				for (Reward reward : quest.getRewards()) {
					CompoundTag rewardTag = new CompoundTag();
					reward.writeToNbt(rewardTag);
					rewardsTag.putCompound(reward.getTemplate().getId(),rewardTag);
				}
				chapterTag.putCompound(quest.getTemplate().getId(),questTag);
			}
			chaptersTag.put(chapter.getTemplate().getId(), chapterTag);
		}
		tag.putCompound("QuestingChapters", chaptersTag);
	}

	@Inject(method = "readAdditionalSaveData",at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		loadData(tag);
	}
}
