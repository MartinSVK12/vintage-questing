package sunsetsatellite.vintagequesting.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeQuestChapter implements ArgumentType<Chapter> {

	public ArgumentTypeQuestChapter() {
	}

	public static ArgumentType<Chapter> chapter() {
		return new ArgumentTypeQuestChapter();
	}

	public Chapter parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (Chapter chapter : getChapters()) {
			if (chapter.getId().equalsIgnoreCase(string)) {
				return chapter;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find chapter: " + string + " (Quest Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for (Chapter chapter : getChapters()) {
			if (chapter.getId().startsWith(builder.getRemaining())) {
				builder.suggest("\"" + chapter.getId() + "\"");
			}
		}

		return builder.buildFuture();
	}

	public Collection<Chapter> getChapters() {
		List<Chapter> list = new ArrayList<>();
		VintageQuesting.CHAPTERS.iterator().forEachRemaining(list::add);
		return list;
	}
}
