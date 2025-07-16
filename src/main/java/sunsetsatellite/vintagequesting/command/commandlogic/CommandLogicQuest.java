package sunsetsatellite.vintagequesting.command.commandlogic;

import com.mojang.brigadier.Command;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.List;

public class CommandLogicQuest {
	public static int completeQuest(Player sender, QuestTemplate questTemplate){
		QuestChapterPage chapter = getChapter(questTemplate);

		if (chapter == null) {
			sender.sendMessage(questTemplate.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(questTemplate);
		quest.forceComplete();

		sender.sendMessage("Quest: "+questTemplate.getTranslatedName()+" has been completed successfully !");
		return Command.SINGLE_SUCCESS;
	}


	public static int completeQuestDeep(Player sender, QuestTemplate questTemplate){
		QuestChapterPage chapter = getChapter(questTemplate);

		if (chapter == null) {
			sender.sendMessage(questTemplate.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(questTemplate);
		if (quest.isCompleted()) return Command.SINGLE_SUCCESS;
		for (Quest prerequisiteQuest : quest.getPreRequisites()) {
			completeQuestDeep(sender, prerequisiteQuest.getTemplate());
		}
		quest.forceComplete();

		sender.sendMessage("Quest: "+questTemplate.getTranslatedName()+" has been completed successfully !");
		return Command.SINGLE_SUCCESS;
	}

	private static QuestChapterPage getChapter(QuestTemplate questTemplate) {
		for (QuestChapterPage chapterPage : VintageQuesting.CHAPTERS) {
			if (chapterPage.hasQuest(questTemplate)) {
				return chapterPage;
			}
		}
		return null;
	}

	public static int resetQuest(Player sender, QuestTemplate questTemplate) {
		QuestChapterPage chapter = getChapter(questTemplate);

		if (chapter == null) {
			sender.sendMessage(questTemplate.getId() + " is not in any chapter pages.");
			return 0;
		}

		Quest quest = chapter.getQuest(questTemplate);
		quest.reset();

		sender.sendMessage("Quest: "+questTemplate.getTranslatedName()+" has been reset successfully !");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetChapter(Player sender, QuestChapterPage chapterPage) {
		List<Quest> quests = chapterPage.getQuests();
		int questAmount = quests.size();
		for (Quest quest : quests) {
			quest.reset();
		}
		sender.sendMessage("Chapter Page: "+chapterPage.getName()+" ("+questAmount+" quests) has been reset successfully !");
		return Command.SINGLE_SUCCESS;
	}

	public static int resetAll(Player sender) {
		int questAmount = 0;
		for (QuestChapterPage chapterPage : VintageQuesting.CHAPTERS) {
			List<Quest> quests = chapterPage.getQuests();
			int chapterQuestAmount = quests.size();
			for (Quest quest : quests) {
				quest.reset();
			}
			questAmount += chapterQuestAmount;
			sender.sendMessage("Chapter Page: "+chapterPage.getName()+" ("+chapterQuestAmount+" quests) has been reset successfully !");
		}
		sender.sendMessage("All the "+questAmount+" quests have been reset successfully !");
		return Command.SINGLE_SUCCESS;
	}
}
