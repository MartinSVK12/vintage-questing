package sunsetsatellite.vintagequesting.client.gui;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.VintageQuestingClient;
import sunsetsatellite.vintagequesting.core.Chapter;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.*;

public abstract class ChapterPage {

	public final Chapter chapter;

	public ChapterPage(Chapter chapter) {
		this.chapter = chapter;
	}

	public abstract @Nullable IconCoordinate getBackgroundTile(ScreenQuestbook screen, int layer, Random random, int tileX, int tileY);

	public abstract void postProcessBackground(ScreenQuestbook screen, Random random, ScreenQuestbook.BGLayer layerCache, int orgX, int orgY);

	public @NotNull ItemStack getIcon() {
		return chapter.getIcon();
	}

	public abstract int backgroundLayers();

	public abstract int backgroundColor();

	public IconCoordinate getQuestBackground(QuestData quest) {
		return TextureRegistry.getTexture(quest.getType().texture());
	}

	public abstract int lineColorLocked(boolean isHovered);

	public abstract int lineColorUnlocked(boolean isHovered);

	public abstract int lineColorCanUnlock(boolean isHovered);

	@NotNull
	public String getDescription() {
		return chapter.getDescription();
	}

	@NotNull
	public String getName() {
		return chapter.getName();
	}

	@NotNull
	public List<Quest> getQuests() {
		return chapter.getQuests();
	}

	@NotNull
	public String getId() {
		return chapter.id;
	}

	public int getOrderId() {
		return chapter.orderId;
	}

	public Quest getStartingQuest() {
		return chapter.getStartingQuest();
	}

	public double getCompletionFraction() {
		return chapter.getCompletionFraction();
	}

	@Nullable
	public Quest getQuest(QuestData quest) {
		return chapter.getQuest(quest);
	}

	public boolean hasQuest(QuestData quest) {
		return chapter.hasQuest(quest);
	}
}
