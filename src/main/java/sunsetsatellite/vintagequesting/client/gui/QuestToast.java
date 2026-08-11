package sunsetsatellite.vintagequesting.client.gui;

import net.minecraft.client.gui.toasts.IToastable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.vintagequesting.core.Quest;

public class QuestToast implements IToastable {

	private static final double ANIMATION_DURATION_MILLIS = 3000L;
	private long startTime;

	public Quest quest;

	public QuestToast(Quest quest) {
		this.quest = quest;
	}

	@Override
	public boolean messageOnly(long l) {
		return false;
	}

	@Override
	public String getTitle(long l) {
		return I18n.getInstance().translateKey("gui.vq.quest.label.complete");
	}

	@Override
	public int titleColor(long runtime) {
		return 0xffffff00;
	}

	@Override
	public String getMessage(long l) {
		return quest.getTranslatedName();
	}

	@Override
	public int messageColor(long runtime) {
		return 0xffffffff;
	}

	@Override
	public double getAnimationProgress(long runtime) {
		runtime = System.currentTimeMillis() - startTime;
		return runtime / ANIMATION_DURATION_MILLIS;
	}

	@Override
	public String getTexture(long l) {
		return "minecraft:gui/toast";
	}

	@Override
	public void onToastStart() {

	}

	@Override
	public void onToastEnd() {

	}

	@Override
	public boolean isEquivalentToast(@NotNull IToastable iToastable) {
		return iToastable instanceof QuestToast && ((QuestToast) iToastable).quest == quest;
	}

	@Override
	public @Nullable ItemStack getIcon(long l) {
		return quest.getIcon().getDefaultStack();
	}
}
