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
import sunsetsatellite.vintagequesting.command.arguments.ArgumentTypeQuestChapterPage;
import sunsetsatellite.vintagequesting.command.arguments.ArgumentTypeQuestId;
import sunsetsatellite.vintagequesting.command.commandlogic.CommandLogicQuest;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

public class CommandQuest implements CommandManager.CommandRegistry {
	public static ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> questComplete(ArgumentBuilder<CommandSource, ArgumentBuilderLiteral<CommandSource>> builder) {
		builder.then(ArgumentBuilderLiteral.<CommandSource>literal("complete").requires(CommandSource::hasAdmin)
			.then(ArgumentBuilderRequired.<CommandSource, QuestTemplate>argument("questId", ArgumentTypeQuestId.questId())
				.then(ArgumentBuilderRequired.<CommandSource, Boolean>argument("deep", ArgumentTypeBool.bool())
					.executes(context ->
					{
						if (context.getArgument("deep", Boolean.class)){
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicQuest.completeQuestDeep(sender, context.getArgument("questId", QuestTemplate.class));
						} else {
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicQuest.completeQuest(sender, context.getArgument("questId", QuestTemplate.class));
						}
					})
				)
				.executes(context ->
					{
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						return CommandLogicQuest.completeQuest(sender, context.getArgument("questId", QuestTemplate.class));
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
				.then(ArgumentBuilderRequired.<CommandSource, QuestTemplate>argument("questId", ArgumentTypeQuestId.questId())
					.executes(context ->
						{
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicQuest.resetQuest(sender, context.getArgument("questId", QuestTemplate.class));
						}
					)
				)
			)
			// Chapter
			.then(ArgumentBuilderLiteral.<CommandSource>literal("chapter")
				.then(ArgumentBuilderRequired.<CommandSource, QuestChapterPage>argument("chapter", ArgumentTypeQuestChapterPage.chapter())
					.executes(context ->
						{
							Player sender = context.getSource().getSender(); if(sender == null){return 0;}
							return CommandLogicQuest.resetChapter(sender, context.getArgument("chapter", QuestChapterPage.class));
						}
					)
				)
			)
			// Everything
			.then(ArgumentBuilderLiteral.<CommandSource>literal("all")
				.executes(context ->
					{
						Player sender = context.getSource().getSender(); if(sender == null){return 0;}
						return CommandLogicQuest.resetAll(sender);
					}
				)
			)
		);


		return builder;
	}

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		System.out.println("Command registered: quest");
		ArgumentBuilderLiteral<CommandSource> builder = ArgumentBuilderLiteral.<CommandSource>literal("quest");

		questComplete(builder);
		questReset(builder);

		dispatcher.register(builder);
	}
}
