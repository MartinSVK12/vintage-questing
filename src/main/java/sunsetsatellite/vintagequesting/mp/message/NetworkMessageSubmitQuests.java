package sunsetsatellite.vintagequesting.mp.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.UUID;

public class NetworkMessageSubmitQuests implements NetworkMessage {

	public NetworkMessageSubmitQuests() {
	}

	@Override
	public void encodeToUniversalPacket(@NonNull UniversalPacket packet) {

	}

	@Override
	public void decodeFromUniversalPacket(@NonNull UniversalPacket packet) {

	}

	@Environment(EnvType.SERVER)
	@Override
	public void handleServerEnv(NetworkContext context) {
		VintageQuesting.submitQuests(context.player);
		IHasQuests player = (IHasQuests) context.player;
		for (UUID member : player.getQuestTeam().members) {
			for (PlayerServer playerServer : MinecraftServer.getInstance().playerList.playerEntities) {
				if(playerServer.uuid.equals(member)) {
					player.synchronizeQuests();
				}
			}
		}
	}
}
