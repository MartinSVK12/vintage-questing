package sunsetsatellite.vintagequesting;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Signal;
import sunsetsatellite.vintagequesting.command.commands.CommandQuest;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.RetrievalTask;
import sunsetsatellite.vintagequesting.core.instance.task.VisitDimensionTask;
import sunsetsatellite.vintagequesting.core.registry.*;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.mp.message.*;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import sunsetsatellite.vintagequesting.util.VQPlugin;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.dependency.Key;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class VintageQuesting implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("vintagequesting", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static TeamRegistry TEAMS = new TeamRegistry();
	public static ChapterRegistry CHAPTERS = new ChapterRegistry();
	public static QuestRegistry QUESTS = new QuestRegistry();
	public static RewardRegistry REWARDS = new RewardRegistry();
	public static TaskRegistry TASKS = new TaskRegistry();

	public static boolean alreadyLoaded = false;

	@Override
	public void onInitialize() {
		Catalyst.DIMENSION_LOAD_SIGNAL.connect(LoadSaveListener.INSTANCE);
		Catalyst.DIMENSION_SAVE_SIGNAL.connect(LoadSaveListener.INSTANCE);
		CommandManager.registerCommand(new CommandQuest());
		NetworkHandler.registerNetworkMessage(NetworkMessageSendTeamData::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageQuestSync::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageSubmitQuests::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageCompleteClickTask::new);
		NetworkHandler.registerNetworkMessage(NetworkMessageReceiveQuestReward::new);
		CommonEvents.AFTER_GAME_START.listen(Key.of(MOD_ID), this::afterGameStart);
		LOGGER.info("Vintage Questing initialized.");
	}


	public void afterGameStart(){
		if(!alreadyLoaded){
			FabricLoader.getInstance().getEntrypointContainers("vintagequesting", VQPlugin.class).stream().map(EntrypointContainer::getEntrypoint).forEach(plugin -> {
				if (plugin.shouldLoad()) plugin.initializePlugin();
			});
		}
	}

	public static void submitQuests(Player player) {
		ArrayList<ItemStack> stacks = Catalyst.condenseItemList(Arrays.stream(player.inventory.mainInventory).collect(Collectors.toList()));
		for (Chapter chapter : ((IHasQuests) player).getQuestTeam().getChapters()) {
			for (Quest chapterQuest : chapter.getQuests()) {
				if (chapterQuest.isCompleted()) continue;
				for (Task task : chapterQuest.getTasks()) {
					if (task instanceof RetrievalTask) {
						((RetrievalTask) task).resetProgress();
						for (ItemStack stack : stacks) {
							((RetrievalTask) task).setProgress(stack, player);
						}
					} else if (task instanceof VisitDimensionTask) {
						((VisitDimensionTask) task).check(player);
					}
				}
			}
		}
	}

	public static class LoadSaveListener implements Signal.Listener<World> {
		public static final Signal.Listener<World> INSTANCE = new LoadSaveListener();

		@Override
		public void signalEmitted(Signal<World> signal, World world) {
			if(EnvironmentHelper.isMultiplayerClient()) return;
			if (signal == Catalyst.DIMENSION_LOAD_SIGNAL) {
				if(world.dimension.id != 0) return;
				if(EnvironmentHelper.isMultiplayerServer() && VintageQuesting.CHAPTERS.size() == 0){
					FabricLoader.getInstance().getEntrypointContainers("vintagequesting", VQPlugin.class).stream().map(EntrypointContainer::getEntrypoint).forEach(plugin -> {
						if (plugin.shouldLoad()) plugin.initializePlugin();
					});
					alreadyLoaded = true;
				}
				File file = world.getLevelStorage().getDataFile("vintagequesting_teams");
				VintageQuesting.TEAMS.clear();
				if (file == null) return;
				if (file.exists()) {
					try {
						CompoundTag tag = NbtIo.readCompressed(Files.newInputStream(file.toPath()));
						for (Tag<?> value : tag.getValues()) {
							if(value instanceof CompoundTag teamTag){
								QuestTeam team = new QuestTeam(teamTag);
								VintageQuesting.TEAMS.put(team.owner,team);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if(signal == Catalyst.DIMENSION_SAVE_SIGNAL) {
				if(world.dimension.id != 0) return;
				try {
					File file = world.getLevelStorage().getDataFile("vintagequesting_teams");
					if (file == null) return;
					CompoundTag teamsTag = new CompoundTag();
					VintageQuesting.TEAMS.forEach((uuid,team)->{
						CompoundTag teamTag = new CompoundTag();
						team.writeToNbt(teamTag);
						team.writeChapters(teamTag);
						teamsTag.putCompound(uuid.toString(), teamTag);
					});
					NbtIo.writeCompressed(teamsTag, Files.newOutputStream(file.toPath()));
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
