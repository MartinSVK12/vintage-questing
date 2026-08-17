package sunsetsatellite.vintagequesting.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.core.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageQuestSync;
import sunsetsatellite.vintagequesting.mp.message.NetworkMessageSendTeamData;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.UUID;

@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixin extends Player implements IHasQuests {

	@Shadow
	public PacketHandlerServer playerNetServerHandler;
	@Shadow
	public MinecraftServer mcServer;
	@Unique
	public QuestTeam questTeam = null;
	@Unique
	public QuestTeam remoteTeam = null;

	private PlayerServerMixin(World world) {
		super(world);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void init(MinecraftServer minecraftserver, World world, String username, UUID uuid, ServerPlayerController serverPlayerController, CallbackInfo ci) {
		remoteTeam = questTeam = VintageQuesting.TEAMS.findMember(uuid).orElseGet(()->{
			QuestTeam team = new QuestTeam(username, uuid);
			VintageQuesting.TEAMS.put(uuid, team);
			return team;
		});
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci){
		if(playerNetServerHandler != null){
			if(remoteTeam != null){
				NetworkHandler.sendToPlayer(this, new NetworkMessageSendTeamData(remoteTeam));
				CompoundTag chapters = new CompoundTag();
				remoteTeam.writeChapters(chapters);
				chapters = chapters.getCompound("Chapters");
				chapters.getValue().forEach((chapterId,tag1)->{
					if(tag1 instanceof CompoundTag chapter){
						chapter.getValue().forEach((questId,tag2)->{
							if(tag2 instanceof CompoundTag quest){
								NetworkHandler.sendToPlayer(this, new NetworkMessageQuestSync(chapterId, quest));
							}
						});
					}
				});
				remoteTeam = null;
			}
		}
	}

	@Override
	public QuestTeam getQuestTeam() {
		return questTeam;
	}

	@Override
	public void setQuestTeam(QuestTeam questTeam) {
		if(questTeam == this.questTeam) return;
		if(this.questTeam != null){
			this.questTeam.members.remove(uuid);
			if(this.questTeam.members.isEmpty()) {
				VintageQuesting.TEAMS.remove(this.questTeam.owner);
			}
		}
		questTeam.members.add(uuid);
		this.questTeam = questTeam;
		synchronizeQuests();
	}

	@Override
	public void synchronizeQuests() {
		remoteTeam = questTeam;
		for (UUID member : questTeam.members) {
			for (PlayerServer playerEntity : mcServer.playerList.playerEntities) {
				if(playerEntity.uuid == member){
					((IHasQuests) playerEntity).synchronizeQuestsForPlayer();
				}
			}
		}
	}

	@Override
	public void synchronizeQuestsForPlayer() {
		remoteTeam = questTeam;
	}
}
