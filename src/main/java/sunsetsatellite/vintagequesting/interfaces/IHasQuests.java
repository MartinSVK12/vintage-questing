package sunsetsatellite.vintagequesting.interfaces;

import sunsetsatellite.vintagequesting.util.QuestTeam;

public interface IHasQuests {

	void setQuestTeam(QuestTeam questTeam);

	QuestTeam getQuestTeam();

	void synchronizeQuestsForPlayer();
	void synchronizeQuests();

}
