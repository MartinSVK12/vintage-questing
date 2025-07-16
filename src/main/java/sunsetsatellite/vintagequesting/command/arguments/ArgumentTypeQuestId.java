package sunsetsatellite.vintagequesting.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeQuestId implements ArgumentType<QuestTemplate> {

	public ArgumentTypeQuestId() {
	}

	public static ArgumentType<QuestTemplate> questId() {
		return new ArgumentTypeQuestId();
	}

	public QuestTemplate parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (QuestTemplate questTemplate : getQuests()) {
			if (questTemplate.getId().equalsIgnoreCase(string)) {
				return questTemplate;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find quest: " + string + " (Quest Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(QuestTemplate questTemplate : getQuests()){
			if (questTemplate.getId().startsWith(builder.getRemaining())) {
				builder.suggest("\""+questTemplate.getId()+"\"");
			}
		}

		return builder.buildFuture();
	}

	public Collection<QuestTemplate> getQuests() {
		List<QuestTemplate> list = new ArrayList<>();
		VintageQuesting.QUESTS.iterator().forEachRemaining(list::add);
		return list;
	}
}
