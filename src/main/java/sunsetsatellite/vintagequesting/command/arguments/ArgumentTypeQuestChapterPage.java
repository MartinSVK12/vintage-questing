package sunsetsatellite.vintagequesting.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.gui.QuestChapterPage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentTypeQuestChapterPage implements ArgumentType<QuestChapterPage> {

	public ArgumentTypeQuestChapterPage() {
	}

	public static ArgumentType<QuestChapterPage> chapter() {
		return new ArgumentTypeQuestChapterPage();
	}

	public QuestChapterPage parse(StringReader reader) throws CommandSyntaxException {
		final String string = reader.readString();

		for (QuestChapterPage chapterPage : getChapters()) {
			if (chapterPage.getId().equalsIgnoreCase(string)) {
				return chapterPage;
			}
		}
		throw new CommandSyntaxException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument(), () -> "Failed to find chapter: " + string + " (Quest Doesn't Exist)");
	}

	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for(QuestChapterPage chapterPage : getChapters()){
			if (chapterPage.getId().startsWith(builder.getRemaining())) {
				builder.suggest("\""+chapterPage.getId()+"\"");
			}
		}

		return builder.buildFuture();
	}

	public Collection<QuestChapterPage> getChapters() {
		List<QuestChapterPage> list = new ArrayList<>();
		VintageQuesting.CHAPTERS.iterator().forEachRemaining(list::add);
		return list;
	}
}
