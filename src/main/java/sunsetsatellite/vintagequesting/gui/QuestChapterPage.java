package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.quest.Quest;
import sunsetsatellite.vintagequesting.quest.template.QuestTemplate;

import java.util.*;

public abstract class QuestChapterPage {
	protected final Map<QuestTemplate, Quest> entryMap = new HashMap<>();
	protected final List<Quest> questList = new ArrayList<>();

	protected final String id;
	protected final int orderId;

	public abstract @NotNull String getName();

	public abstract @NotNull String getDescription();

	public QuestChapterPage(String id) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = 0;
	}

	public QuestChapterPage(String id, int orderId) {
		VintageQuesting.CHAPTERS.register(id, this);
		this.id = id;
		this.orderId = orderId;
	}

	public void addQuest(@NotNull QuestTemplate quest) {
		Quest entry = new Quest(quest, this);
		questList.add(entry);
		entryMap.put(quest, entry);
	}

	public void loadQuests(List<Quest> quests) {
		questList.clear();
		entryMap.clear();
		for (Quest quest : quests) {
			if (quest.getPage() == this) {
				questList.add(quest);
				entryMap.put(quest.getTemplate(), quest);
			}
		}
	}

	public void reset() {
		Set<QuestTemplate> quests = new HashSet<>(entryMap.keySet());
		questList.clear();
		entryMap.clear();
		for (QuestTemplate quest : quests) {
			addQuest(quest);
		}
		for (Quest quest : getQuests()) {
			quest.setupPrerequisites();
		}
	}

	public abstract @Nullable IconCoordinate getBackgroundTile(ScreenQuestbook screen, int layer, Random random, int tileX, int tileY);

	public abstract void postProcessBackground(ScreenQuestbook screen, Random random, ScreenQuestbook.BGLayer layerCache, int orgX, int orgY);

	public abstract @NotNull ItemStack getIcon();

	public @NotNull Set<QuestTemplate> getQuestTemplates() {
		return entryMap.keySet();
	}

	public @NotNull List<Quest> getQuests() {
		return questList;
	}

	public @Nullable Quest getQuest(QuestTemplate quest) {
		return entryMap.get(quest);
	}

	public double getCompletionFraction() {
		int completed = 0;
		for (Quest q : questList) {
			if (q.isCompleted()) {
				completed++;
			}
		}
		return completed / (double) questList.size();
	}

	public abstract int backgroundLayers();

	public abstract int backgroundColor();

	public boolean hasQuest(QuestTemplate quest) {
		return entryMap.containsKey(quest);
	}

	public abstract Quest getStartingQuest();

	public abstract IconCoordinate getQuestBackground(QuestTemplate quest);

	public abstract int lineColorLocked(boolean isHovered);

	public abstract int lineColorUnlocked(boolean isHovered);

	public abstract int lineColorCanUnlock(boolean isHovered);

    /*public static IconCoordinate getTextureFromBlock(Block<?> block) {
        return BlockModelDispatcher.getInstance().getDispatch(block).getBlockTextureFromSideAndMetadata(Side.TOP, 0);
    }*/

	public String getId() {
		return id;
	}

	public int getOrderId() {
		return orderId;
	}
}
