package sunsetsatellite.vintagequesting.core.registry;

import sunsetsatellite.vintagequesting.util.QuestTeam;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class TeamRegistry extends HashMap<UUID, QuestTeam> {

	public Optional<QuestTeam> findMember(UUID uuid){
		return values().stream().filter(questTeam -> questTeam.members.contains(uuid)).findFirst();
	}

}
