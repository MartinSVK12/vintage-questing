package sunsetsatellite.vintagequesting.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeBool;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.command.arguments.ArgumentTypeQuestChapter;
import sunsetsatellite.vintagequesting.command.arguments.ArgumentTypeQuestId;
import sunsetsatellite.vintagequesting.command.commandlogic.CommandLogicQuest;
import sunsetsatellite.vintagequesting.command.commandlogic.CommandLogicQuestTeam;
import sunsetsatellite.vintagequesting.core.data.ChapterData;
import sunsetsatellite.vintagequesting.core.data.QuestData;

public class CommandQuest implements CommandManager.CommandRegistry {
	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> questComplete(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("complete").requires(CommandSource::hasAdmin)
			.then(ArgumentBuilderRequired.<CommandSource, QuestData>argument("questId", ArgumentTypeQuestId.questId())
				.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("deep", ArgumentTypeBool.bool())
					.executes(context ->
					{
						if (context.getArgument("deep", Boolean.class)) {
							Player sender = context.getSource().getSender();
							if (sender == null) {
								return 0;
							}
							return CommandLogicQuest.completeQuestDeep(sender, context.getArgument("questId", QuestData.class));
						} else {
							Player sender = context.getSource().getSender();
							if (sender == null) {
								return 0;
							}
							return CommandLogicQuest.completeQuest(sender, context.getArgument("questId", QuestData.class));
						}
					})
				)
				.executes(context ->
					{
						Player sender = context.getSource().getSender();
						if (sender == null) {
							return 0;
						}
						return CommandLogicQuest.completeQuest(sender, context.getArgument("questId", QuestData.class));
					}
				)
			)
		);

		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> questReset(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("reset").requires(CommandSource::hasAdmin)
			// Quest
			.then(ArgumentBuilderLiteral.<CommandSource>literal("quest")
				.then(ArgumentBuilderRequired.<CommandSource, QuestData>argument("questId", ArgumentTypeQuestId.questId())
					.executes(context ->
						{
							Player sender = context.getSource().getSender();
							if (sender == null) {
								return 0;
							}
							return CommandLogicQuest.resetQuest(sender, context.getArgument("questId", QuestData.class));
						}
					)
				)
			)
			// Chapter
			.then(ArgumentBuilderLiteral.<CommandSource>literal("chapter")
				.then(ArgumentBuilderRequired.<CommandSource, ChapterData>argument("chapter", ArgumentTypeQuestChapter.chapter())
					.executes(context ->
						{
							Player sender = context.getSource().getSender();
							if (sender == null) {
								return 0;
							}
							return CommandLogicQuest.resetChapter(sender, context.getArgument("chapter", ChapterData.class));
						}
					)
				)
			)
			// Everything
			.then(ArgumentBuilderLiteral.<CommandSource>literal("all")
				.executes(context ->
					{
						Player sender = context.getSource().getSender();
						if (sender == null) {
							return 0;
						}
						return CommandLogicQuest.resetAll(sender);
					}
				)
			)
		);


		return builder;
	}

	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> team(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder
			.then(ArgumentBuilderLiteral.<CommandSource>literal("team")
				.then(ArgumentBuilderLiteral.<CommandSource>literal("invite")
					.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
						.executes(CommandLogicQuestTeam::invitePlayer)
					)
					.then(ArgumentBuilderLiteral.<CommandSource>literal("accept")
						.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
							.executes(CommandLogicQuestTeam::acceptInvite))
					.then(ArgumentBuilderLiteral.<CommandSource>literal("decline")
						.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
							.executes(CommandLogicQuestTeam::declineInvite))
					.then(ArgumentBuilderLiteral.<CommandSource>literal("cancel")
						.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
							.executes(CommandLogicQuestTeam::cancelInvite)
					)
				)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("kick")
					.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
						.executes(CommandLogicQuestTeam::kickFromTeam)
				))
				.then(ArgumentBuilderLiteral.<CommandSource>literal("leave")
					.executes(CommandLogicQuestTeam::leaveTeam)
				)
				.then(ArgumentBuilderLiteral.<CommandSource>literal("rename")
					.then(ArgumentBuilderRequired.<CommandSource,String>argument("name", ArgumentTypeString.word())
						.executes(CommandLogicQuestTeam::renameTeam)
					)
				)
			))));
		return builder;
	}


	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		VintageQuesting.LOGGER.info("Command registered: quest");
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.literal("quest");

		questComplete(builder);
		questReset(builder);
		team(builder);

		dispatcher.register(builder);
	}
}
