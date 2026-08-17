package sunsetsatellite.vintagequesting.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.Session;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.VintageQuestingClient;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.EnvironmentHelper;

@Mixin(value = PlayerLocal.class, remap = false)
public abstract class PlayerLocalMixin extends Player implements IHasQuests {

	@Unique
	public QuestTeam questTeam = null;

	private PlayerLocalMixin(World world) {
		super(world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(Minecraft minecraft, World world, Session session, int dimension, CallbackInfo ci) {
		if(EnvironmentHelper.isSingleplayerClient()){
			if(VintageQuesting.TEAMS.get(uuid) == null){
				QuestTeam team = new QuestTeam(username, uuid);
				VintageQuesting.TEAMS.put(uuid, team);
				questTeam = VintageQuestingClient.LOCAL_TEAM = team;
				return;
			}
			questTeam = VintageQuestingClient.LOCAL_TEAM = VintageQuesting.TEAMS.get(uuid);
			VintageQuestingClient.reloadPages();
		} else if(EnvironmentHelper.isMultiplayerClient()){
			questTeam = VintageQuestingClient.LOCAL_TEAM = new QuestTeam(username, uuid);
			VintageQuestingClient.reloadPages();
		}
	}

	@Override
	public void setQuestTeam(QuestTeam questTeam) {
		if(this.questTeam != null){
			this.questTeam.members.remove(uuid);
			if(this.questTeam.members.isEmpty()) {
				VintageQuesting.TEAMS.remove(this.questTeam.owner);
			}
		}
		questTeam.members.add(uuid);
		this.questTeam = questTeam;
		VintageQuestingClient.LOCAL_TEAM = questTeam;
		VintageQuestingClient.reloadPages();
	}

	@Override
	public QuestTeam getQuestTeam() {
		return questTeam;
	}

	@Override
	public void synchronizeQuests() {

	}

	@Override
	public void synchronizeQuestsForPlayer() {

	}
}
