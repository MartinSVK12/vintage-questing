package sunsetsatellite.vintagequesting.interfaces;

import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.util.QuestGroup;

public interface IHasQuests {

	QuestGroup getQuestGroup();

	void setCurrentChapter(Chapter chapter);

	Chapter getCurrentChapter();
}
