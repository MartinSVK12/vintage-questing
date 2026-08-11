package sunsetsatellite.vintagequesting.command.commandlogic;

import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.client.gui.ChapterPage;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.List;

public class CommandLogicQuest {
	public static int completeQuest(Player sender, QuestData QuestData) {
		Chapter chapter = getChapter(QuestData);

		if (chapter == null) {
			sender.sendMessage(QuestData.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(QuestData);
		quest.forceComplete();

		sender.sendMessage("Quest \"" + QuestData.getTranslatedName() + "\" has been completed successfully!");
		return Command.SINGLE_SUCCESS;
	}


	public static int completeQuestDeep(Player sender, QuestData QuestData) {
		Chapter chapter = getChapter(QuestData);

		if (chapter == null) {
			sender.sendMessage(QuestData.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(QuestData);
		if (quest.isCompleted()) return Command.SINGLE_SUCCESS;
		for (Quest prerequisiteQuest : quest.getPreRequisites()) {
			completeQuestDeep(sender, prerequisiteQuest.data);
		}
		quest.forceComplete();

		sender.sendMessage("Quest \"" + QuestData.getTranslatedName() + "\" has been completed successfully!");
		return Command.SINGLE_SUCCESS;
	}

	private static Chapter getChapter(QuestData QuestData) {
		for (Chapter chapterPage : VintageQuesting.CHAPTERS) {
			if (chapterPage.hasQuest(QuestData)) {
				return chapterPage;
			}
		}
		return null;
	}

	public static int resetQuest(Player sender, QuestData QuestData) {
		Chapter chapter = getChapter(QuestData);

		if (chapter == null) {
			sender.sendMessage(QuestData.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(QuestData);
		quest.reset();

		sender.sendMessage("Quest \"" + QuestData.getTranslatedName() + "\" has been reset successfully!");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetChapter(Player sender, Chapter chapter) {
		List<Quest> quests = chapter.getQuests();
		int questAmount = quests.size();
		for (Quest quest : quests) {
			quest.reset();
		}
		sender.sendMessage("Chapter page \"" + chapter.getName() + "\" (" + questAmount + " quests) has been reset successfully!");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetAll(Player sender) {
		int questAmount = 0;
		for (Chapter chapterPage : VintageQuesting.CHAPTERS) {
			List<Quest> quests = chapterPage.getQuests();
			int chapterQuestAmount = quests.size();
			for (Quest quest : quests) {
				quest.reset();
			}
			questAmount += chapterQuestAmount;
			sender.sendMessage("Chapter page: " + chapterPage.getName() + " (" + chapterQuestAmount + " quests) has been reset successfully!");
		}
		sender.sendMessage("All the " + questAmount + " quests have been reset successfully !");
		return Command.SINGLE_SUCCESS;
	}
}
