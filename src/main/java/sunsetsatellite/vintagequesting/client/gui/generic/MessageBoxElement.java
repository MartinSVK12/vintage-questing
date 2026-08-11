package sunsetsatellite.vintagequesting.client.gui.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Scissor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageBoxElement
	extends Gui {
	private static final String FORMAT_REGEX = "[" + TextFormatting.FORMAT_CHARS + "]";
	private final @NotNull Minecraft minecraft = Minecraft.getMinecraft();
	private float scrollAmount = 0;
	private final @NotNull List<@NotNull String> lines = new ArrayList<>();
	private final int height;
	private final int width;

	private int scrollbarX;
	private int scrollbarY;
	private int scrollbarWidth;
	private int scrollbarHeight;
	private boolean isScrolling = false;
	private int clickY;
	private float previousScrollAmount = 0.0f;

	public MessageBoxElement(final int width, final int height, final @NotNull String text, final int chars) {
		this.width = width;
		this.height = height;

		this.setupText(text, chars);
	}

	private void setupText(final @NotNull String text, int limit) {
		limit = Math.max(limit, 1);
		this.lines.clear();

		final @NotNull List<@NotNull String> completeSplit = new ArrayList<>(); //the processed wrapped text, each entry is one line
		final @NotNull ArrayList<@NotNull String> newlineSplit = new ArrayList<>(Arrays.asList(text.split("\\n"))); //text split based only on newlines before processing

		@NotNull String lastFormat = "";

		for (final @NotNull String s : newlineSplit) {
			final @NotNull ArrayList<@NotNull String> words = new ArrayList<>(Arrays.asList(s.split(" ")));
			final @NotNull ArrayList<@NotNull String> limitedSizeWords = new ArrayList<>(); //all words here should not be larger than the limit
			@NotNull StringBuilder line = new StringBuilder();
			line.append(lastFormat);
			//split words larger than the limit into multiple that can fit
			for (final @NotNull String word : words) {
				if (word.length() > limit) {
					final @NotNull ArrayList<@NotNull String> split = new ArrayList<>();
					for (int j = 0; j <= word.length() / limit; j++) {
						split.add(word.substring(j * limit, Math.min((j + 1) * limit, word.length())));
					}
					limitedSizeWords.addAll(split);
				} else {
					limitedSizeWords.add(word);
				}
			}
			for (final @NotNull String word : limitedSizeWords) {
				@NotNull String currentFormat = "";
				//check for formatting and extract it
				if (word.contains("§")) {
					if (word.indexOf('§') < word.length() - 1) {
						final @NotNull String format = String.valueOf(word.charAt(word.indexOf('§') + 1));
						if (format.matches(FORMAT_REGEX)) { //if matches any valid text format
							currentFormat = "§" + format;
						}
					}
				}
				if (line.length() + 1 + word.length() < limit) {
					//if line has not hit the char limit yet
					line.append(word).append(" ");
				} else {
					//line has hit the limit, wrap it preserving the last formatting
					completeSplit.add(line.toString());
					line = new StringBuilder();
					line.append(lastFormat);
					line.append(word).append(" ");
				}
				if (!currentFormat.isEmpty())
					lastFormat = currentFormat; //change last format if a new one was found in this iteration
			}
			completeSplit.add(line.toString());
		}

		this.lines.addAll(completeSplit);
	}

	public int getHeight() {
		return this.height;
	}

	private void scroll(final float amount) {
		this.scrollAmount = MathHelper.clamp(this.scrollAmount + amount, 0.0f, 1.0f);
	}

	public void render(final int x, final int y, final int mouseX, final int mouseY) {
		// Do scroll
		if (this.mouseInRegion(x, y, mouseX, mouseY)) {
			final float wheel = Mouse.getDWheel();
			if (wheel != 0.0f)
				this.scroll(wheel / -12.0f);
		}

		GLRenderer.pushFrame();
		GLRenderer.setShader(Shaders.COLOR);
		this.drawBackground(x, y);
		this.drawScrollbar(x, y, mouseX, mouseY);

		Scissor.enable(x + 1, y + 1, this.getWidth() - 2, this.getHeight() - 2);

		// Draw lines
		for (int i = 0; i < this.lines.size(); i++) {
			this.minecraft.font.render(this.lines.get(i), x + 4, (y + 4) + (i * 12) - this.getScrollPixels()).setShadow().call();
		}

		Scissor.disable();
		GLRenderer.popFrame();
	}

	private boolean mouseInRegion(final int x, final int y, final int mouseX, final int mouseY) {
		return mouseX >= x && mouseX < x + this.getWidth() && mouseY >= y && mouseY < y + this.height;
	}

	private int getScrollPixels() {
		return (int) (this.scrollAmount * (this.getScrollableHeight() - (this.height - 2)));
	}

	private int getScrollableHeight() {
		return Math.max(20 * this.lines.size(), this.height);
	}

	public void onClick(final int x, final int y, final int button) {
		if (button == 0 && x >= this.scrollbarX && x < this.scrollbarX + this.scrollbarWidth && y >= this.scrollbarY && y < this.scrollbarY + this.scrollbarHeight) {
			this.isScrolling = true;
			this.previousScrollAmount = this.scrollAmount;
			this.clickY = y;
		}
	}

	public void mouseMovedOrUp(final int x, final int y, final int button) {
		if (button == 0) {
			this.isScrolling = false;
			this.previousScrollAmount = 0.0f;
			this.clickY = 0;
		}
	}

	private void drawBackground(final int x, final int y) {
		final TessellatorGeneral tessellator = GLRenderer.getTessellator();
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque1i(0xA0A0A0);
		tessellator.addVertex(x, y + this.height, 0.0D);
		tessellator.addVertex(x + this.getWidth(), y + this.height, 0.0D);
		tessellator.addVertex(x + this.getWidth(), y, 0.0D);
		tessellator.addVertex(x, y, 0.0D);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque1i(0x000000);
		tessellator.addVertex(x + 1, y + this.height - 1, 0.0D);
		tessellator.addVertex(x + this.getWidth() - 1, y + this.height - 1, 0.0D);
		tessellator.addVertex(x + this.getWidth() - 1, y + 1, 0.0D);
		tessellator.addVertex(x + 1, y + 1, 0.0D);
		tessellator.draw();
	}

	private void drawScrollbar(final int x, final int y, final int mouseX, final int mouseY) {
		final int scrollableHeight = this.getScrollableHeight();
		final int displayRegionHeight = this.height - 2;
		final float scrollbarScale = (float) displayRegionHeight / scrollableHeight;

		this.scrollbarWidth = 6;
		this.scrollbarHeight = (int) (scrollbarScale * displayRegionHeight);

		final int minScrollbarY = 0;
		final int maxScrollbarY = displayRegionHeight - this.scrollbarHeight;

		final int scrollbarDelta = maxScrollbarY - minScrollbarY;

		this.scrollbarY = y + 1 + (int) (this.scrollAmount * scrollbarDelta);
		this.scrollbarX = x + this.getWidth() - 1 - 6;

		final @NotNull TessellatorGeneral t = GLRenderer.getTessellator();

		t.startDrawingQuads();
		t.setColor2i(0x808080, 255);
		t.drawRectangle(this.scrollbarX, this.scrollbarY, 6, this.scrollbarHeight);
		t.setColor2i(0xc0c0c0, 255);
		t.drawRectangle(this.scrollbarX, this.scrollbarY, 5, this.scrollbarHeight - 1);
		t.draw();

		if (this.isScrolling) {
			final int delta = mouseY - this.clickY;
			final float scrolledScreens = delta / (float) this.scrollbarHeight;
			final float scrolledPixels = displayRegionHeight * scrolledScreens;
			final float scrolledAmount = scrolledPixels / (float) (scrollableHeight - displayRegionHeight);

			this.scrollAmount = MathHelper.clamp(this.previousScrollAmount + scrolledAmount, 0.0f, 1.0f);
		}
	}

	public int getWidth() {
		return width;
	}
}
