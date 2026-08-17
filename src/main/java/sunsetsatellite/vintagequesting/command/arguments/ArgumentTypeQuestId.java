package sunsetsatellite.vintagequesting.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeQuestId implements ArgumentType<QuestData> {

	public ArgumentTypeQuestId() {
	}

	public static ArgumentType<QuestData> questId() {
		return new ArgumentTypeQuestId();
	}

	public QuestData parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (QuestData QuestData : getQuests()) {
			if (QuestData.getId().equalsIgnoreCase(string)) {
				return QuestData;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find quest: " + string + " (Quest doesn't exist.)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for (QuestData QuestData : getQuests()) {
			if (QuestData.getId().startsWith(builder.getRemaining())) {
				builder.suggest("\"" + QuestData.getId() + "\"");
			}
		}

		return builder.buildFuture();
	}

	public Collection<QuestData> getQuests() {
		List<QuestData> list = new ArrayList<>();
		VintageQuesting.QUESTS.iterator().forEachRemaining(list::add);
		return list;
	}
}
