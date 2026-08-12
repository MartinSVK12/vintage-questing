package sunsetsatellite.vintagequesting.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.data.ChapterData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeQuestChapter implements ArgumentType<ChapterData> {

	public ArgumentTypeQuestChapter() {
	}

	public static ArgumentType<ChapterData> chapter() {
		return new ArgumentTypeQuestChapter();
	}

	public ChapterData parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (ChapterData chapter : getChapters()) {
			if (chapter.getId().equalsIgnoreCase(string)) {
				return chapter;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find chapter: " + string + " (Quest Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for (ChapterData chapter : getChapters()) {
			if (chapter.getId().startsWith(builder.getRemaining())) {
				builder.suggest("\"" + chapter.getId() + "\"");
			}
		}

		return builder.buildFuture();
	}

	public Collection<ChapterData> getChapters() {
		List<ChapterData> list = new ArrayList<>();
		VintageQuesting.CHAPTERS.iterator().forEachRemaining(list::add);
		return list;
	}
}
