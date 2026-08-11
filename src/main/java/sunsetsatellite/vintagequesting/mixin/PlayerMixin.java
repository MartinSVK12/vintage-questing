package sunsetsatellite.vintagequesting.mixin;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Reward;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.data.QuestData;
import sunsetsatellite.vintagequesting.core.data.RewardData;
import sunsetsatellite.vintagequesting.core.data.TaskData;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageQuestSync;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = Player.class, remap = false)
public class PlayerMixin {

	@Shadow
	@Final
	@NotNull
	public ContainerInventory inventory;
	@Unique
	private final Player thisAs = (Player) ((Object) this);

	@Unique
	private List<NetworkMessageQuestSync> questPackets = new ArrayList<>();

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(World world, CallbackInfo ci) {
		VintageQuesting.LOGGER.info("Initializing quests...");
		resetAll();
	}

	@Environment(EnvType.SERVER)
	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci){
		if(thisAs instanceof PlayerServer playerServer){
			if(playerServer.playerNetServerHandler != null){
				for (NetworkMessageQuestSync packet : questPackets) {
					NetworkHandler.sendToPlayer(thisAs, packet);
				}
				questPackets.clear();
			}
		}
	}

	@Unique
	public void loadData(CompoundTag tag) {
		resetAll();

		CompoundTag chapters = tag.getCompoundOrDefault("QuestingChapters", null);
		if (chapters != null) {
			Map<String, Tag<?>> chapterMap = chapters.getValue();
			if (chapters.getValues().isEmpty()) {
				//VintageQuesting.LOGGER.warn("No data. Loading defaults...");
				for (Chapter chapter : VintageQuesting.CHAPTERS) {
					//questGroup.quests.addAll(chapter.getQuests());
				}
				return;
			}
			for (Map.Entry<String, Tag<?>> entry : chapterMap.entrySet()) {
				String id = entry.getKey();
				Tag<?> mapTag = entry.getValue();
				CompoundTag chapterTag = ((CompoundTag) mapTag);
				Chapter chapter = VintageQuesting.CHAPTERS.getItem(id);
				if (chapter != null) {
					for (Quest quest : chapter.getQuests()) {
						CompoundTag questTag = chapterTag.getCompoundOrDefault(quest.data.getId(), null);
						if (questTag != null) {
							if(EnvironmentHelper.isMultiplayerServer()){
								questPackets.add(new NetworkMessageQuestSync(chapter.getId(), questTag));
							}
							CompoundTag tasks = questTag.getCompound("Tasks");
							CompoundTag rewards = questTag.getCompound("Rewards");
							quest.readFromNbt(questTag);
							for (Reward reward : quest.getRewards()) {
								CompoundTag rewardTag = rewards.getCompoundOrDefault(reward.data.getId(), null);
								if (rewardTag != null) {
									reward.readFromNbt(rewardTag);
								} else {
									VintageQuesting.LOGGER.error("No reward with id: " + reward.data.getId());
								}
							}
							for (Task task : quest.getTasks()) {
								CompoundTag taskTag = tasks.getCompoundOrDefault(task.data.getId(), null);
								if (taskTag != null) {
									task.readFromNbt(taskTag);
								} else {
									VintageQuesting.LOGGER.error("No task with id: " + task.data.getId());
								}
							}
						} else {
							VintageQuesting.LOGGER.error("No quest with id: " + quest.data.getId());
						}
					}
					VintageQuesting.LOGGER.info("Loaded saved data for chapter with id: " + id);
				} else {
					VintageQuesting.LOGGER.error("No chapter with id: " + id);
				}
			}
		} else {
			//VintageQuesting.LOGGER.warn("No data. Loading defaults...");
			for (Chapter chapter : VintageQuesting.CHAPTERS) {
				//questGroup.quests.addAll(chapter.getQuests());
			}
		}
	}

	@Unique
	public void resetAll() {
		for (Chapter chapter : VintageQuesting.CHAPTERS) {
			chapter.reset();
			for (QuestData template : chapter.getQuestData()) {
				for (RewardData reward : template.getRewards()) {
					reward.clearInstance();
				}
				for (TaskData task : template.getTasks()) {
					task.clearInstance();
				}
			}
		}
	}

	@Unique
	public void resetChapter(String id) {
		Chapter chapter = VintageQuesting.CHAPTERS.getItem(id);
		if (chapter == null) return;
		chapter.reset();
		for (QuestData template : chapter.getQuestData()) {
			for (RewardData reward : template.getRewards()) {
				reward.clearInstance();
			}
			for (TaskData task : template.getTasks()) {
				task.clearInstance();
			}
		}
	}

	@Unique
	public void resetQuest(String id) {
		QuestData quest = VintageQuesting.QUESTS.getItem(id);
		if (quest == null) return;
		for (RewardData reward : quest.getRewards()) {
			reward.clearInstance();
		}
		for (TaskData task : quest.getTasks()) {
			task.clearInstance();
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		CompoundTag chaptersTag = new CompoundTag();
		for (Chapter chapter : VintageQuesting.CHAPTERS) {
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
					tasksTag.putCompound(task.data.getId(), taskTag);
				}
				for (Reward reward : quest.getRewards()) {
					CompoundTag rewardTag = new CompoundTag();
					reward.writeToNbt(rewardTag);
					rewardsTag.putCompound(reward.data.getId(), rewardTag);
				}
				chapterTag.putCompound(quest.data.getId(), questTag);
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
