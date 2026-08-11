package sunsetsatellite.vintagequesting.mp.message;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.vintagequesting.VintageQuesting;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

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
	}
}
