package sunsetsatellite.vintagequesting.interfaces;

import com.mojang.nbt.CompoundTag;
import sunsetsatellite.vintagequesting.quest.Chapter;
import sunsetsatellite.vintagequesting.util.QuestGroup;

public interface IHasQuests {

	QuestGroup getQuestGroup();

	void setQuestGroup(QuestGroup group);

	void setCurrentChapter(Chapter chapter);

	Chapter getCurrentChapter();

	void loadData(CompoundTag tag);

	void resetAll();

	void resetChapter(String id);

	void resetQuest(String id);
}
