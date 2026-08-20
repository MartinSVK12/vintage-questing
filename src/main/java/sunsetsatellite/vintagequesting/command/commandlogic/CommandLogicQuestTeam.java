package sunsetsatellite.vintagequesting.command.commandlogic;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.exceptions.CommandExceptions;
import net.minecraft.core.util.helper.UUIDHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.command.IServerCommandSource;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.interfaces.IHasQuests;
import sunsetsatellite.vintagequesting.util.QuestTeam;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.Objects;
import java.util.UUID;

public class CommandLogicQuestTeam {
	public static int renameTeam(CommandContext<CommandSource> c) {
		Player sender = c.getSource().getSender();
		if (sender == null) {
			return 0;
		}
		IHasQuests quests = (IHasQuests) sender;

		QuestTeam team = quests.getQuestTeam();
		if (!Objects.equals(sender.uuid, team.owner)) {
			sender.sendMessage("You are not the owner of this team!");
			return 0;
		}
		team.name = c.getArgument("name", String.class);
		quests.synchronizeQuests();
		sender.sendMessage(String.format("Renamed team to \"%s\"!",team.name));
		return Command.SINGLE_SUCCESS;
	}

	public static int leaveTeam(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if (sender == null) return 0;
		IHasQuests quests = (IHasQuests) sender;
		if(Objects.equals(quests.getQuestTeam().owner, sender.uuid)) {
			sender.sendMessage("Cannot leave your own team!");
			return 0;
		}
		QuestTeam team = new QuestTeam(sender.username, sender.uuid);
		VintageQuesting.TEAMS.put(sender.uuid, team);
		quests.setQuestTeam(team);
		sender.sendMessage(String.format("Left team \"%s\" successfully!",team.name));
		return Command.SINGLE_SUCCESS;
	}

	public static int listTeam(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if (sender == null) return 0;
		IHasQuests quests = (IHasQuests) sender;
		QuestTeam team = quests.getQuestTeam();
		sender.sendMessage(String.format("Members of team \"%s\":", team.name));
		for (UUID member : team.members) {
			for (PlayerServer player : MinecraftServer.getInstance().playerList.playerEntities) {
				if(player.uuid.equals(member)){
					sender.sendMessage("- "+player.username);
				}
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	public static int kickFromTeam(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if(sender == null) return 0;

		MinecraftServer server = ((IServerCommandSource)source).getServer();

		String name = c.getArgument("name", String.class);
		PlayerServer otherPlayer = server.playerList.getPlayerEntity(name);

		if(otherPlayer != null && otherPlayer != c.getSource().getSender()) {
			IHasQuests quests = (IHasQuests) otherPlayer;
			if (!Objects.equals(sender.uuid, ((IHasQuests) sender).getQuestTeam().owner)) {
				sender.sendMessage("You are not the owner of this team!");
				return 0;
			}
			QuestTeam team = new QuestTeam(otherPlayer.username, otherPlayer.uuid);
			VintageQuesting.TEAMS.put(otherPlayer.uuid, team);
			quests.setQuestTeam(team);
			sender.sendMessage(String.format("Kicked %s from team \"%s\" successfully!",otherPlayer.username, team.name));
			otherPlayer.sendMessage(String.format("You have been kicked from the \"%s\" quest team.",team.name));
			return Command.SINGLE_SUCCESS;
		} else {
			if(otherPlayer == null){
				sender.sendMessage("Invalid player!");
				return 0;
			}
			if(otherPlayer == c.getSource().getSender()) {
				sender.sendMessage("Cannot kick yourself!");
				return 0;
			}
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int cancelInvite(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if(sender == null) return 0;

		MinecraftServer server = ((IServerCommandSource)source).getServer();

		String name = c.getArgument("name", String.class);
		PlayerServer otherPlayer = server.playerList.getPlayerEntity(name);

		if(otherPlayer != null && otherPlayer != c.getSource().getSender()) {
			IHasQuests playerQuests = (IHasQuests) sender;
			QuestTeam team = playerQuests.getQuestTeam();
			if(!team.invites.contains(otherPlayer.uuid)){
				sender.sendMessage(String.format("Nothing to cancel, you have not invited %s before.", otherPlayer.username));
				return 0;
			}
			sender.sendMessage(String.format("Cancelled the invitation for %s successfully!",otherPlayer.username));
			team.invites.remove(otherPlayer.uuid);
		} else {
			sender.sendMessage("Invalid player!");
			return 0;
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int declineInvite(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if(sender == null) return 0;

		MinecraftServer server = ((IServerCommandSource)source).getServer();

		String name = c.getArgument("name", String.class);
		PlayerServer otherPlayer = server.playerList.getPlayerEntity(name);

		if(otherPlayer != null && otherPlayer != c.getSource().getSender()) {
			IHasQuests otherPlayerQuests = (IHasQuests) otherPlayer;
			QuestTeam otherTeam = otherPlayerQuests.getQuestTeam();
			if(!otherTeam.invites.contains(sender.uuid)){
				sender.sendMessage("You do not have any invites from "+otherPlayer.username+"!");
				return 0;
			}
			otherTeam.invites.remove(sender.uuid);
			otherPlayer.sendMessage(String.format("%s has declined your invite.", sender.username));
		} else {
			if(otherPlayer == null){
				sender.sendMessage("Invalid player!");
				return 0;
			}
			if(otherPlayer == c.getSource().getSender()) {
				sender.sendMessage("Cannot accept or decline invites from yourself!");
				return 0;
			}
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int acceptInvite(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if(sender == null) return 0;

		MinecraftServer server = ((IServerCommandSource)source).getServer();

		String name = c.getArgument("name", String.class);
		PlayerServer otherPlayer = server.playerList.getPlayerEntity(name);

		if(otherPlayer != null && otherPlayer != c.getSource().getSender()) {
			IHasQuests otherPlayerQuests = (IHasQuests) otherPlayer;
			QuestTeam otherTeam = otherPlayerQuests.getQuestTeam();
			IHasQuests playerQuests = (IHasQuests) sender;
			if(!otherTeam.invites.contains(sender.uuid)){
				sender.sendMessage("You do not have any invites from "+otherPlayer.username+"!");
				return 0;
			}
			otherTeam.invites.remove(sender.uuid);
			playerQuests.setQuestTeam(otherTeam);
			otherPlayer.sendMessage(String.format("%s has accepted your invite!", sender.username));
		} else {
			if(otherPlayer == null){
				sender.sendMessage("Invalid player!");
				return 0;
			}
			if(otherPlayer == c.getSource().getSender()) {
				sender.sendMessage("Cannot accept or decline invites from yourself!");
				return 0;
			}
		}

		return Command.SINGLE_SUCCESS;
	}

	public static int invitePlayer(CommandContext<CommandSource> c) throws CommandSyntaxException {
		CommandSource source = c.getSource();
		if (EnvironmentHelper.isSingleplayerClient()) throw CommandExceptions.multiplayerWorldOnly().create();
		Player sender = c.getSource().getSender();
		if(sender == null) return 0;

		MinecraftServer server = ((IServerCommandSource)source).getServer();

		String name = c.getArgument("name", String.class);
		PlayerServer otherPlayer = server.playerList.getPlayerEntity(name);

		if(otherPlayer != null && otherPlayer != c.getSource().getSender()) {
			IHasQuests quests = (IHasQuests) sender;
			QuestTeam team = quests.getQuestTeam();
			if (!Objects.equals(sender.uuid, team.owner)) {
				sender.sendMessage("You are not the owner of this team!");
				return 0;
			}
			if(team.members.contains(otherPlayer.uuid)){
				sender.sendMessage(String.format("%s is already a member of your team!", otherPlayer.username));
				return 0;
			}
			team.invites.add(otherPlayer.uuid);
			otherPlayer.sendMessage(String.format("%s has invited you to join their quest team.", sender.username));
			otherPlayer.sendMessage(String.format("Accept with: /quest team invite accept %s", sender.username));
			otherPlayer.sendMessage(String.format("Decline with: /quest team invite decline %s", sender.username));
			sender.sendMessage("Sent invite to "+otherPlayer.username+"!");
			return Command.SINGLE_SUCCESS;
		} else {
			if(otherPlayer == null){
				sender.sendMessage("Invalid player!");
				return 0;
			}
			if(otherPlayer == c.getSource().getSender()) {
				sender.sendMessage("Cannot invite yourself!");
				return 0;
			}
		}

		return Command.SINGLE_SUCCESS;
	}
}
