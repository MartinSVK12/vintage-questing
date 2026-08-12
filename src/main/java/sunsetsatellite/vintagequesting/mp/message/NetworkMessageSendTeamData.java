package sunsetsatellite.vintagequesting.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.Task;
import sunsetsatellite.vintagequesting.core.instance.task.ClickTask;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.Objects;

public class NetworkMessageSendTeamData implements NetworkMessage {

	public QuestTeam team;

	public NetworkMessageSendTeamData() {

	}

	public NetworkMessageSendTeamData(QuestTeam team) {
		this.team = team;
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {
		packet.writeCompoundTag(team.writeToNbt(new CompoundTag()));
	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {
		team = new QuestTeam(packet.readCompoundTag());
	}

	@Override
	public void handleClientEnv(NetworkContext context) {
		((IHasQuests) context.player).setQuestTeam(team);
	}
}
