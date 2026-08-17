package sunsetsatellite.vintagequesting.command.commandlogic;

import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.client.gui.ChapterPage;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.data.ChapterData;
import sunsetsatellite.vintagequesting.core.data.QuestData;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;

import java.util.List;

public class CommandLogicQuest {
	public static int completeQuest(Player sender, QuestData QuestData) {
		ChapterData chapterData = getChapter(QuestData);

		if (chapterData == null) {
			sender.sendMessage(QuestData.getId() + " is not in any chapter pages.");
			return 0;
		}

		IHasQuests quests = (IHasQuests) sender;
		Chapter chapter = quests.getQuestTeam().chapters.get(chapterData.id);

		Quest quest = chapter.getQuest(QuestData);
		quest.forceComplete();
		quests.synchronizeQuests();
		sender.sendMessage("Quest \"" + QuestData.getTranslatedName() + "\" has been completed successfully!");
		return Command.SINGLE_SUCCESS;
	}


	public static int completeQuestDeep(Player sender, QuestData questData) {
		ChapterData chapterData = getChapter(questData);

		if (chapterData == null) {
			sender.sendMessage(questData.getId() + " is not in any chapter pages.");
			return 0;
		}

		IHasQuests quests = (IHasQuests) sender;
		Chapter chapter = quests.getQuestTeam().chapters.get(chapterData.id);
		Quest quest = chapter.getQuest(questData);
		if (quest.isCompleted()) return Command.SINGLE_SUCCESS;
		for (Quest prerequisiteQuest : quest.getPreRequisites()) {
			completeQuestDeep(sender, prerequisiteQuest.data);
		}
		quest.forceComplete();
		quests.synchronizeQuests();
		sender.sendMessage("Quest \"" + questData.getTranslatedName() + "\" has been completed successfully!");
		return Command.SINGLE_SUCCESS;
	}

	private static ChapterData getChapter(QuestData QuestData) {
		for (ChapterData chapterPage : VintageQuesting.CHAPTERS) {
			if (chapterPage.hasQuest(QuestData)) {
				return chapterPage;
			}
		}
		return null;
	}

	public static int resetQuest(Player sender, QuestData QuestData) {
		ChapterData chapterData = getChapter(QuestData);

		if (chapterData == null) {
			sender.sendMessage(QuestData.getId() + " is not in any chapter pages.");
			return 0;
		}

		IHasQuests quests = (IHasQuests) sender;
		Chapter chapter = quests.getQuestTeam().chapters.get(chapterData.id);
		Quest quest = chapter.getQuest(QuestData);
		quest.reset();
		quests.synchronizeQuests();
		sender.sendMessage("Quest \"" + QuestData.getTranslatedName() + "\" has been reset successfully!");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetChapter(Player sender, ChapterData chapterData) {
		IHasQuests player = (IHasQuests) sender;
		Chapter chapter = player.getQuestTeam().chapters.get(chapterData.id);
		chapter.reset();
		player.synchronizeQuests();
		sender.sendMessage("Chapter page \"" + chapter.getName() + "\" has been reset successfully!");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetAll(Player sender) {
		IHasQuests quests = (IHasQuests) sender;
		quests.getQuestTeam().reset();
		quests.synchronizeQuests();
		sender.sendMessage("All quests have been reset successfully!");
		return Command.SINGLE_SUCCESS;
	}
}
