package sunsetsatellite.vintagequesting.client.gui;


import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.achievements.data.AchievementPageRegistry;
import net.minecraft.client.gui.options.OptionsButtonElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.Scissor;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.*;
import net.minecraft.client.render.tessellator.RenderBuffer;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL41;
import sunsetsatellite.vintagequesting.VintageQuesting;
import sunsetsatellite.vintagequesting.VintageQuestingClient;
import sunsetsatellite.vintagequesting.core.Quest;
import sunsetsatellite.vintagequesting.core.data.QuestData;

import java.util.*;

public class ScreenQuestbook extends Screen {
	private static final int TOP_SPACING = 24;
	private static final int BUTTON_SPACING = 4;
	private static final int SEPARATOR_WIDTH = 8;
	private static final int PADDING = 8;
	private static final int PAGE_BUTTON_HEIGHT = 20;

	private static final int ACHIEVEMENT_CELL_WIDTH = 24;
	private static final int ACHIEVEMENT_CELL_HEIGHT = 24;

	private static final int ACHIEVEMENT_ICON_WIDTH = 26;
	private static final int ACHIEVEMENT_ICON_HEIGHT = 26;

	private static final int TOOLTIP_BOX_WIDTH_MIN = 120;
	private static final int TOOLTIP_OFF_X = 8;
	private static final int TOOLTIP_OFF_Y = -4;

	protected int mouseXOld;
	protected int mouseYOld;
	protected double oldShiftX;
	protected double oldShiftY;
	protected double targetShiftX;
	protected double targetShiftY;
	protected double currentShiftX;
	protected double currentShiftY;
	private boolean draggingViewport;
	private final TooltipElement tooltip;
	private ItemElement renderItem = null;
	Screen parent;

	private int top;
	private int bottom;

	private int viewportLeft;
	private int viewportTop;
	private int viewportRight;
	private int viewportBottom;
	private int viewportWidth;
	private int viewportHeight;

	private double viewportZoom = 1;

	private double shiftMinX;
	private double shiftMinY;
	private double shiftMaxX;
	private double shiftMaxY;

	private int pageListLeft;
	private int pageListRight;

	private float pageListScrollAmount = 0.0f;
	private Float oldPagesListScrollAmount;
	private int pagesListScrollRegionHeight;

	private Integer clickX, clickY;

	private ChapterPage hoveredPage = null;
	private Quest hoveredQuest = null;

	private ChapterPage currentPage;

	private BGLayer[] layers;
	private final List<ChapterPage> chapters = new ArrayList<>();

	public ScreenQuestbook(Screen parent, ChapterPage page) {
		this.mouseXOld = 0;
		this.mouseYOld = 0;
		this.draggingViewport = false;
		currentPage = page;

		this.parent = parent;
		this.tooltip = new TooltipElement(this.mc);
		this.renderItem = new ItemElement(this.mc);

		layers = new BGLayer[currentPage.backgroundLayers()];
		for (int i = 0; i < layers.length; i++) {
			layers[i] = new BGLayer(i);
		}

		VintageQuestingClient.CHAPTER_PAGES.forEach(chapters::add);
		chapters.sort(Comparator.comparingInt(ChapterPage::getOrderId));
	}

	@Override
	public void init() {
		this.buttons.clear();
		this.buttons.add(new OptionsButtonElement(1, this.width / 2 - 100, this.height - 20 - BUTTON_SPACING, 200, 20, I18n.getInstance().translateKey("gui.achievements.button.done")));

		this.lastTileX = Integer.MIN_VALUE;
		this.lastTileY = Integer.MIN_VALUE;

		this.top = TOP_SPACING;
		this.bottom = this.height - (BUTTON_SPACING + 20 + BUTTON_SPACING);

		this.pagesListScrollRegionHeight = this.bottom - this.top;
		this.pageListLeft = 0;
		this.pageListRight = this.width / 4;

		this.viewportZoom = 1;

		this.viewportLeft = drawSidebar() ? this.pageListRight + SEPARATOR_WIDTH : 0;
		this.viewportTop = this.top;
		this.viewportBottom = this.bottom;
		this.viewportRight = this.width;

		this.viewportWidth = this.viewportRight - this.viewportLeft;
		this.viewportHeight = this.viewportBottom - this.viewportTop;

		int achMinX = Integer.MAX_VALUE;
		int achMinY = Integer.MAX_VALUE;
		int achMaxX = Integer.MIN_VALUE;
		int achMaxY = Integer.MIN_VALUE;

		for (Quest q : currentPage.getQuests()) {
			if (q.getX() < achMinX) {
				achMinX = q.getX();
			}
			if (q.getY() < achMinY) {
				achMinY = q.getY();
			}
			if (q.getX() > achMaxX) {
				achMaxX = q.getX();
			}
			if (q.getY() > achMaxY) {
				achMaxY = q.getY();
			}
		}

		this.shiftMinX = achMinX * ACHIEVEMENT_CELL_WIDTH;
		this.shiftMinY = achMinY * ACHIEVEMENT_CELL_HEIGHT;
		this.shiftMaxX = achMaxX * ACHIEVEMENT_CELL_WIDTH + ACHIEVEMENT_CELL_WIDTH;
		this.shiftMaxY = achMaxY * ACHIEVEMENT_CELL_HEIGHT + ACHIEVEMENT_CELL_HEIGHT;

		this.shiftMinX -= (int) (this.viewportWidth / 4d);
		this.shiftMinY -= (int) (this.viewportHeight / 4d);
		this.shiftMaxX += (int) (this.viewportWidth / 4d);
		this.shiftMaxY += (int) (this.viewportHeight / 4d);

		// Centers the screen on the Open ContainerInventory achievement
		Quest q = currentPage.getStartingQuest();
		this.oldShiftX = this.targetShiftX = this.currentShiftX = q.getX() * ACHIEVEMENT_CELL_WIDTH + ACHIEVEMENT_CELL_WIDTH / 2d;
		this.oldShiftY = this.targetShiftY = this.currentShiftY = q.getY() * ACHIEVEMENT_CELL_HEIGHT + ACHIEVEMENT_CELL_HEIGHT / 2d;
	}

	@Override
	public void removed() {
		if (this.lastBackgroundShadowBuf != null) {
			this.lastBackgroundShadowBuf.delete();
			this.lastBackgroundShadowBuf = null;
		}
		if (this.lastBackgroundTileBuf != null) {
			this.lastBackgroundTileBuf.delete();
			this.lastBackgroundTileBuf = null;
		}
	}

	@Override
	protected void buttonClicked(@NotNull ButtonElement button) {
		if (button.id == 1) {
			this.mc.displayScreen(this.parent);
			//mc.setIngameFocus();
		}
		super.buttonClicked(button);
	}

	@Override
	public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			this.mc.displayScreen(this.parent);
		} else {
			super.keyPressed(eventCharacter, eventKey, mx, my);
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int buttonNum) {
		if (drawSidebar() && mx >= this.pageListLeft && mx <= (this.pageListRight - 6) && my >= this.top && my <= this.bottom) {
			int pagesListHeight = getTotalPagesListHeight();
			int pagesListY = this.top - (int) this.pageListScrollAmount;
			if (pagesListHeight < this.bottom - this.top) {
				pagesListY = this.top + (this.bottom - this.top - pagesListHeight) / 2;
			}
			for (ChapterPage page : chapters) {
				if (mx >= this.pageListLeft && mx <= (this.pageListRight - 6) && my >= pagesListY && my <= pagesListY + PAGE_BUTTON_HEIGHT) {
					this.currentPage = page;

					if (this.lastBackgroundShadowBuf != null) {
						this.lastBackgroundShadowBuf.delete();
						this.lastBackgroundShadowBuf = null;
					}
					if (this.lastBackgroundTileBuf != null) {
						this.lastBackgroundTileBuf.delete();
						this.lastBackgroundTileBuf = null;
					}

					this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);

					layers = new BGLayer[currentPage.backgroundLayers()];
					for (int i = 0; i < layers.length; i++) {
						layers[i] = new BGLayer(i);
					}

					init();
					break;
				}
				pagesListY += PAGE_BUTTON_HEIGHT;
			}
		}

		if (hoveredQuest != null) {
			mc.displayScreen(new ScreenQuestInfo(this, hoveredQuest));
		}

		super.mouseClicked(mx, my, buttonNum);

		this.clickX = mx;
		this.clickY = my;
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		if (Mouse.isButtonDown(0)) {
			if (mx >= this.viewportLeft && mx < this.viewportRight && my >= this.viewportTop && my < this.viewportBottom) {
				if (!this.draggingViewport) {
					this.draggingViewport = true;
				} else {
					this.targetShiftX -= (mx - this.mouseXOld) / this.viewportZoom;
					this.targetShiftY -= (my - this.mouseYOld) / this.viewportZoom;
					this.currentShiftX = this.oldShiftX = this.targetShiftX;
					this.currentShiftY = this.oldShiftY = this.targetShiftY;
				}
				this.mouseXOld = mx;
				this.mouseYOld = my;
			}
			this.currentShiftX = MathHelper.clamp(this.currentShiftX, this.shiftMinX, this.shiftMaxX);
			this.currentShiftY = MathHelper.clamp(this.currentShiftY, this.shiftMinY, this.shiftMaxY);
		} else if (this.mc.controllerInput != null) {
			this.targetShiftX += this.mc.controllerInput.joyRight.getX() / this.viewportZoom * 4;
			this.targetShiftY += this.mc.controllerInput.joyRight.getY() / this.viewportZoom * 4;
			this.currentShiftX = this.oldShiftX = this.targetShiftX;
			this.currentShiftY = this.oldShiftY = this.targetShiftY;
			this.currentShiftX = MathHelper.clamp(this.currentShiftX, this.shiftMinX, this.shiftMaxX);
			this.currentShiftY = MathHelper.clamp(this.currentShiftY, this.shiftMinY, this.shiftMaxY);

			if (this.mc.controllerInput.buttonLeftTrigger.isPressed()) {
				this.viewportZoom -= 0.01d;
			} else if (this.mc.controllerInput.buttonRightTrigger.isPressed()) {
				this.viewportZoom += 0.01f;
			}
			this.viewportZoom = MathHelper.clamp(this.viewportZoom, 0.5d, 2d);

		} else {
			this.clickX = this.clickY = null;
			this.oldPagesListScrollAmount = null;
			this.draggingViewport = false;
		}

		if (drawSidebar() && mx >= this.pageListLeft && mx <= this.pageListRight) {
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				scrollPagesList(Mouse.getDWheel() / -0.01f);
			} else {
				scrollPagesList(Mouse.getDWheel() / -0.05f);
			}
			onScrollPagesList();
		} else if (mx >= this.viewportLeft && mx <= this.viewportRight && my >= this.viewportTop && my <= this.viewportBottom) {
			final double change = (Mouse.getDWheel() / 10d);
			this.viewportZoom = MathHelper.clamp(this.viewportZoom + change, 0.5d, 2);

			// Make zoom notch onto integer multiples
			if (change != 0) {
				final double[] notches = new double[]{0.25, 0.5, 1, 2, 4};
				for (double notch : notches) {
					if (Math.abs(this.viewportZoom - notch) < 0.05) {
						this.viewportZoom = notch;
						break;
					}
				}
			}
		}
		Mouse.getDWheel();

		renderBackground();

		if (drawSidebar()) {
			overlayBackground(0, this.pageListRight, this.top, this.bottom, 0x202020);
		}

		renderAchievementsPanel(mx, my, partialTick);

		overlayBackground(0, this.width, 0, this.top, 0x404040);
		overlayBackground(0, this.width, this.bottom, this.height, 0x404040);
		overlayBackground(this.pageListRight, this.viewportLeft, this.top, this.bottom, 0x404040);

		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

		super.render(mx, my, partialTick); // Draw Buttons
		GLRenderer.enableState(State.DEPTH_TEST);
		GLRenderer.globalSetLightEnabled(true);
		Lighting.disable();

		GLRenderer.globalSetLightEnabled(false);
		GLRenderer.disableState(State.DEPTH_TEST);

		if (drawSidebar()) {
			Scissor.enable(this.pageListLeft, this.top, this.pageListRight - this.pageListLeft, this.bottom - this.top);
			int pagesListHeight = getTotalPagesListHeight();
			int pagesListY = this.top - (int) this.pageListScrollAmount;
			if (pagesListHeight < this.bottom - this.top) {
				pagesListY = this.top + (this.bottom - this.top - pagesListHeight) / 2;
			}
			if (my >= this.top && my <= this.bottom) {
				this.hoveredPage = drawPagesListItems(this.pageListLeft + PADDING - 4, pagesListY, this.pageListRight - PADDING, mx, my);
			} else {
				this.hoveredPage = drawPagesListItems(this.pageListLeft + PADDING - 4, pagesListY, this.pageListRight - PADDING, -1, -1);
			}
			Scissor.disable();
		}

		{
			GLRenderer.pushFrame();
			GLRenderer.setShader(Shaders.COLOR);
			GLRenderer.enableState(State.BLEND);
			GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);

			byte fadeDist = 4;
			TessellatorGeneral tessellator = GLRenderer.getTessellator();
			if (drawSidebar()) {
				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(this.pageListLeft, this.top + fadeDist, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.pageListRight, this.top + fadeDist, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(this.pageListRight, this.top, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(this.pageListLeft, this.top, 0.0D, 0.0D, 0.0D);
				tessellator.draw();

				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(this.pageListLeft, this.bottom, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.pageListRight, this.bottom, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(this.pageListRight, this.bottom - fadeDist, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(this.pageListLeft, this.bottom - fadeDist, 0.0D, 0.0D, 0.0D);
				tessellator.draw();

				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(this.viewportLeft, this.top + fadeDist, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.viewportRight, this.top + fadeDist, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(this.viewportRight, this.top, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(this.viewportLeft, this.top, 0.0D, 0.0D, 0.0D);
				tessellator.draw();

				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(this.viewportLeft, this.bottom, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.viewportRight, this.bottom, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(this.viewportRight, this.bottom - fadeDist, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(this.viewportLeft, this.bottom - fadeDist, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
			} else {
				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(0, this.top + fadeDist, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.width, this.top + fadeDist, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(this.width, this.top, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(0, this.top, 0.0D, 0.0D, 0.0D);
				tessellator.draw();

				tessellator.startDrawingQuads();
				tessellator.setColor2i(0, 255);
				tessellator.addVertexWithUV(0, this.bottom, 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(this.width, this.bottom, 0.0D, 1.0D, 1.0D);
				tessellator.setColor2i(0, 0);
				tessellator.addVertexWithUV(this.width, this.bottom - fadeDist, 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(0, this.bottom - fadeDist, 0.0D, 0.0D, 0.0D);
				tessellator.draw();
			}
			GLRenderer.popFrame();
		}

		if (this.hoveredQuest != null) {
			drawAchievementToolTip(this.hoveredQuest, mx, my);
		}

		if (drawSidebar()) {
			drawPagesListScrollBar(mx, my);

			if (this.hoveredPage != null) {
				final String msg = this.hoveredPage.getDescription() + "\n" + TextFormatting.LIGHT_GRAY + I18n.getInstance().translateKeyAndFormat("gui.achievements.label.completion", Math.round(this.hoveredPage.getCompletionFraction() * 100) + "%");
				this.tooltip.render(msg, mx, my, TOOLTIP_OFF_X, TOOLTIP_OFF_Y, TOOLTIP_BOX_WIDTH_MIN, -1, true);
			}
		}

		renderLabels();
		GLRenderer.enableState(State.DEPTH_TEST);

		this.hoveredPage = null;
	}

	@Override
	public void tick() {
		this.oldShiftX = this.targetShiftX;
		this.oldShiftY = this.targetShiftY;
		double xDiff = this.currentShiftX - this.targetShiftX;
		double yDiff = this.currentShiftY - this.targetShiftY;
		if (xDiff * xDiff + yDiff * yDiff < 4D) {
			this.targetShiftX += xDiff;
			this.targetShiftY += yDiff;
		} else {
			this.targetShiftX += xDiff * 0.85D;
			this.targetShiftY += yDiff * 0.85D;
		}
	}

	protected void renderLabels() {
		drawStringCenteredNoShadow(this.fontRenderer, I18n.getInstance().translateKey("gui.vq.questbook.label.title")/* + " " + viewportZoom + " X:" + currentShiftX + ", Y:" + currentShiftY*/, this.width / 2, 5, 0xFFFFFF);
	}


	protected void renderAchievementsPanel(int mouseX, int mouseY, float partialTick) {
		double shiftX = MathHelper.lerp(this.oldShiftX, this.targetShiftX, partialTick);
		double shiftY = MathHelper.lerp(this.oldShiftY, this.targetShiftY, partialTick);
		shiftX = MathHelper.clamp(shiftX, this.shiftMinX, this.shiftMaxX);
		shiftY = MathHelper.clamp(shiftY, this.shiftMinY, this.shiftMaxY);

		this.zLevel = 0.0F;

		GLRenderer.setDepthFunc(CompareFunc.GREATER_EQUAL);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(0, 0, -200F);
		Scissor.enable(this.viewportLeft, this.viewportTop, this.viewportWidth, this.viewportHeight);
		GLRenderer.globalSetLightEnabled(false);
		drawRectDouble(this.viewportLeft, this.viewportTop, this.viewportRight, this.viewportBottom, 0xFF000000 | this.currentPage.backgroundColor()); // Ensures that the viewport always has a background of some kind

		GLRenderer.pushFrame();
		drawBackgroundTiles(shiftX, shiftY);

		GLRenderer.enableState(State.DEPTH_TEST);
		GLRenderer.setDepthFunc(CompareFunc.LESS_EQUAL); // Responsible for culling the overdraw later on
		drawConnectingLines(mouseX, mouseY, shiftX, shiftY);

		Lighting.enableInventoryLight();
		GLRenderer.globalSetLightEnabled(false);
		this.hoveredQuest = drawAchievementIcons(mouseX, mouseY, shiftX, shiftY);

		GLRenderer.popFrame();

		GLRenderer.disableState(State.DEPTH_TEST);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Scissor.disable();

//        drawRectDouble(viewportLeft + viewportWidth/2d - 2.5, viewportTop + viewportHeight/2d - 2.5, viewportLeft + viewportWidth/2d + 2.5, viewportTop + viewportHeight/2d + 2.5, 0xFFA0A0A0); // Debug cross-hair

		GLRenderer.popFrame();

		this.zLevel = 0.0F;
		GLRenderer.setDepthFunc(CompareFunc.LESS_EQUAL);
		GLRenderer.disableState(State.DEPTH_TEST);
	}

	private static final int TILE_WIDTH = 16;
	private static final int TILE_HEIGHT = 16;

	public int lastTileX = Integer.MIN_VALUE;
	public int lastTileY = Integer.MIN_VALUE;
	public int lastTilesWide = Integer.MIN_VALUE;
	public int lastTilesTall = Integer.MIN_VALUE;
	public @Nullable RenderBuffer lastBackgroundTileBuf;
	public @Nullable RenderBuffer lastBackgroundShadowBuf;

	private void drawBackgroundTiles(double shiftX, double shiftY) {
		double zoom = this.viewportZoom/* * 0.85*/;

		TextureRegistry.worldAtlas.bind();
		final int offset = 18 * TILE_WIDTH;
		int viewTileX = (MathHelper.floor(shiftX) + offset) / TILE_WIDTH;
		int viewTileY = (MathHelper.floor(shiftY) + offset) / TILE_HEIGHT;
		double remainderX = (shiftX + offset) % TILE_WIDTH;
		double remainderY = (shiftY + offset) % TILE_HEIGHT;
		Random random = new Random();

		int tilesWide = (int) (this.viewportWidth / (TILE_WIDTH * zoom) + 2);
		int tilesTall = (int) (this.viewportHeight / (TILE_HEIGHT * zoom) + 2);

		int orgX = -tilesWide / 2 - 1;
		int orgY = -tilesTall / 2 - 1;
		int endX = tilesWide / 2 + 1;
		int endY = tilesTall / 2 + 1;

		tilesWide = endX - orgX;
		tilesTall = endY - orgY;

		// Cache background, saves some render time which is nice
		boolean dirty = false;
		if (viewTileX != this.lastTileX || viewTileY != this.lastTileY || tilesWide != this.lastTilesWide || tilesTall != this.lastTilesTall) {
			this.lastTileX = viewTileX;
			this.lastTileY = viewTileY;
			this.lastTilesWide = tilesWide;
			this.lastTilesTall = tilesTall;
			dirty = true;

			for (BGLayer layer : this.layers) {
				layer.resize(tilesWide, tilesTall);
			}

			long worldSeed = this.mc.currentWorld == null ? 0 : this.mc.currentWorld.getRandomSeed();
			for (int _y = 0; _y < tilesTall; _y++) {
				for (int _x = 0; _x < tilesWide; _x++) {
					int tileX = orgX + _x + viewTileX;
					int tileY = orgY + _y + viewTileY;
					// Hopefully this is actually random enough :)
					random.setSeed(worldSeed);
					long l1 = random.nextLong();
					random.setSeed(tileX);
					long l2 = random.nextLong();
					random.setSeed(tileY);
					long l3 = random.nextLong();

					long seed = Objects.hash(l1, l2, ~l3);

					for (BGLayer layer : this.layers) {
						random.setSeed(seed);
						IconCoordinate fore = this.currentPage.getBackgroundTile(this, layer.id, random, tileX, tileY);
						layer.put(fore, _x, _y);
					}
				}
			}


//            long l1 = random.nextLong();
//            random.setSeed(viewTileX);
//            long l2 = random.nextLong();
//            random.setSeed(viewTileY);
//            long l3 = random.nextLong();
//
//            long seed = Objects.hash(l1, l2, ~l3);
//            random.setSeed(seed);

			for (BGLayer layer : this.layers) {
				random.setSeed(worldSeed);
				this.currentPage.postProcessBackground(this, random, layer, orgX + viewTileX, orgY + viewTileY);
			}
		}


		if (dirty) {
			for (int renderpass = 0; renderpass < 2; renderpass++) {
				TessellatorGeneral t = GLRenderer.getTessellator();
				if (renderpass == 0) {
					t.startDrawingQuads();
				} else {
					t.startDrawing(DrawMode.TRIANGLES);
				}
				for (int _y = 0; _y < tilesTall; _y++) {
					int tileY = orgY + _y + viewTileY;
					float brightness = 0.6F - ((float) (tileY) / 25F) * 0.3F;
					for (int _x = 0; _x < tilesWide; _x++) {
						int tileX = orgX + _x + viewTileX;

						for (int i = this.layers.length - 1; i >= 0; i--) {
							BGLayer topLayer = getLayer(i);
							IconCoordinate fore = topLayer.get(_x, _y);

							IconCoordinate next = null;
							if (i - 1 >= 0) {
								BGLayer nextLayer = getLayer(i - 1);
								next = nextLayer.get(_x, _y);
							}


							boolean bottom = false;
							boolean top = false;
							boolean left = false;
							boolean right = false;
							boolean topLeft = false;
							boolean topRight = false;
							boolean bottomLeft = false;
							boolean bottomRight = false;


							if (fore != null && next == null && i - 1 >= 0) {
								BGLayer nextLayer = getLayer(i - 1);
								top = nextLayer.get(_x, _y - 1) != null;
								left = nextLayer.get(_x - 1, _y) != null;
								right = nextLayer.get(_x + 1, _y) != null;
								bottom = nextLayer.get(_x, _y + 1) != null;
								topLeft = nextLayer.get(_x - 1, _y - 1) != null;
								topRight = nextLayer.get(_x + 1, _y - 1) != null;
								bottomLeft = nextLayer.get(_x - 1, _y + 1) != null;
								bottomRight = nextLayer.get(_x + 1, _y + 1) != null;
							}

							double iconLeft = _x * TILE_WIDTH;
							double iconTop = _y * TILE_WIDTH;
							double iconWidth = TILE_WIDTH;
							double iconHeight = TILE_HEIGHT;


							if (renderpass == 0) { // Background block tile quads pass
								if (fore != null) {
									float shadowScale = (float) Math.pow(0.65f, i);
									if (next != null) {
										shadowScale *= 0.5f;
									}
									t.setColor4f(brightness * shadowScale, brightness * shadowScale, brightness * shadowScale, 1.0F);
									addGuiIconDouble(t, iconLeft, iconTop, iconWidth, iconHeight, fore);
								}
							} else { // Background shadow tri pass
								final double off = 0;
								double fadeDist = 6 * zoom;
								short shadowDarkness = 128;

								if (top) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft - off, iconTop + fadeDist + off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth + off, iconTop + fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop - off, 0.0D);

									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop - off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth + off, iconTop + fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth + off, iconTop - off, 0.0D);
								}
								if (left) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + fadeDist + off, iconTop + iconHeight + off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + fadeDist + off, iconTop - off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop + iconHeight + off, 0.0D);


									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop + iconHeight + off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + fadeDist + off, iconTop - off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop - off, 0.0D);
								}
								if (bottom) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth + off, iconTop + iconHeight - fadeDist + off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft - off, iconTop + iconHeight - fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth + off, iconTop + iconHeight - off, 0.0D);

									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth + off, iconTop + iconHeight - off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft - off, iconTop + iconHeight - fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop + iconHeight - off, 0.0D);
								}
								if (right) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth - fadeDist + off, iconTop - off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth - fadeDist + off, iconTop + iconHeight + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth - off, iconTop - off, 0.0D);

									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth - off, iconTop - off, 0.0D);
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth - fadeDist + off, iconTop + iconHeight + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth - off, iconTop + iconHeight + off, 0.0D);
								}
								if (topLeft && !(left || top)) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft - off, iconTop + fadeDist + off, 0.0D);
									t.addVertex(iconLeft + fadeDist + off, iconTop - off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop - off, 0.0D);
								}
								if (topRight && !(right || top)) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth - fadeDist + off, iconTop - off, 0.0D);
									t.addVertex(iconLeft + iconWidth - off, iconTop + fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth - off, iconTop - off, 0.0D);
								}
								if (bottomLeft && !(left || bottom)) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + fadeDist + off, iconTop + iconHeight - off, 0.0D);
									t.addVertex(iconLeft - off, iconTop + iconHeight - fadeDist + off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft - off, iconTop + iconHeight - off, 0.0D);
								}
								if (bottomRight && !(right || bottom)) {
									t.setColor2i(0, 0);
									t.addVertex(iconLeft + iconWidth - off, iconTop + iconHeight - fadeDist + off, 0.0D);
									t.addVertex(iconLeft + iconWidth - fadeDist + off, iconTop + iconHeight - off, 0.0D);
									t.setColor2i(0, shadowDarkness);
									t.addVertex(iconLeft + iconWidth - off, iconTop + iconHeight - off, 0.0D);
								}
							}
						}
					}
				}
				if (renderpass == 0) {
					if (this.lastBackgroundTileBuf == null) {
						this.lastBackgroundTileBuf = t.record(GL41.glGenVertexArrays(), GL41.glGenBuffers());
					} else {
						this.lastBackgroundTileBuf = t.record(this.lastBackgroundTileBuf.vao(), this.lastBackgroundTileBuf.vbo());
					}
				} else {
					if (this.lastBackgroundShadowBuf == null) {
						this.lastBackgroundShadowBuf = t.record(GL41.glGenVertexArrays(), GL41.glGenBuffers());
					} else {
						this.lastBackgroundShadowBuf = t.record(this.lastBackgroundShadowBuf.vao(), this.lastBackgroundShadowBuf.vbo());
					}
				}
			}
		}
		double iconLeft = (this.viewportLeft + this.viewportWidth / 2d) + zoom * (orgX * TILE_WIDTH) - zoom * remainderX;
		double iconTop = (this.viewportTop + this.viewportHeight / 2d) + zoom * ((orgY * TILE_HEIGHT) - remainderY);

		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate((float) iconLeft, (float) iconTop, 0).scale((float) zoom, (float) zoom, 1);
		if (this.lastBackgroundTileBuf != null) {
			TextureRegistry.worldAtlas.bind();
			GLRenderer.render(this.lastBackgroundTileBuf);
		}
		if (this.lastBackgroundShadowBuf != null) {
			GLRenderer.pushFrame();
			GLRenderer.setShader(Shaders.COLOR);
			GLRenderer.enableState(State.BLEND);
			GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
			GLRenderer.render(this.lastBackgroundShadowBuf);
			GLRenderer.popFrame();
		}
		GLRenderer.popFrame();
	}

	public BGLayer getLayer(int layer) {
		if (layer < 0 || layer >= this.layers.length) return null;
		return this.layers[layer];
	}

	private double timeSin(double amplitude, long period) {
		return Math.sin(((double) (System.currentTimeMillis() % period) / period) * Math.PI * 2D) * amplitude;
	}

	private void drawConnectingLines(int mouseX, int mouseY, double shiftX, double shiftY) {
		drawConnectingLinesPass(mouseX, mouseY, shiftX, shiftY, false);
		drawConnectingLinesPass(mouseX, mouseY, shiftX, shiftY, true);
	}

	private void drawConnectingLinesPass(int mouseX, int mouseY, double shiftX, double shiftY, boolean unlockedPass) {
		double zoom = this.viewportZoom;

		for (Quest entry : currentPage.getQuests()) {
			QuestData quest = entry.data;
			List<QuestData> preRequisites = quest.getPreRequisites();
			for (QuestData parent : preRequisites) {
				Quest parentEntry = currentPage.getQuest(parent);
				if (parentEntry == null) {
					continue;
				}
				if (!currentPage.hasQuest(quest) || !currentPage.hasQuest(parent)) {
					continue;
				}
				boolean unlocked = entry.isCompleted();
				boolean canUnlock = entry.preRequisitesCompleted();
				double childX = (this.viewportLeft + this.viewportWidth / 2d) + ((entry.getX() * ACHIEVEMENT_CELL_WIDTH - shiftX) + 11) * zoom;
				double childY = (this.viewportTop + this.viewportHeight / 2d) + ((entry.getY() * ACHIEVEMENT_CELL_HEIGHT - shiftY) + 11) * zoom;
				double parentX = (this.viewportLeft + this.viewportWidth / 2d) + ((parentEntry.getX() * ACHIEVEMENT_CELL_WIDTH - shiftX) + 11) * zoom;
				double parentY = (this.viewportTop + this.viewportHeight / 2d) + ((parentEntry.getY() * ACHIEVEMENT_CELL_HEIGHT - shiftY) + 11) * zoom;

				final double zoomOff = 11 * zoom;

				boolean isHovered = false;
				{
					double x = parentX - zoomOff;
					double y = parentY - zoomOff;
					if ((mouseX >= 0 && mouseY >= this.viewportTop && mouseX < this.width && mouseY < this.viewportBottom) && // In viewport and
						(mouseX >= x && mouseX <= x + 22 * zoom && mouseY >= y && mouseY <= y + 22 * zoom)) { // Hovering over achievement
						isHovered = true;
					}

					x = childX - zoomOff;
					y = childY - zoomOff;
					if ((mouseX >= 0 && mouseY >= this.viewportTop && mouseX < this.width && mouseY < this.viewportBottom) && // In viewport and
						(mouseX >= x && mouseX <= x + 22 * zoom && mouseY >= y && mouseY <= y + 22 * zoom)) { // Hovering over achievement
						isHovered = true;
					}
				}

				int color;
				if (unlocked) {
					color = 0xff << Color.SHIFT_ALPHA | (this.currentPage.lineColorUnlocked(isHovered) & 0xffffff);
				} else if (canUnlock) {
					int alpha = timeSin(1, 600) >= 0.6 ? 0x82 : 0xff;
					color = (alpha << Color.SHIFT_ALPHA) | (this.currentPage.lineColorCanUnlock(isHovered) & 0xffffff);
				} else {
					color = 0xff << Color.SHIFT_ALPHA | (this.currentPage.lineColorLocked(isHovered) & 0xffffff);
				}

				drawLineHorizontalDouble(childX, parentX, childY, color);
				drawLineVerticalDouble(parentX, childY, parentY, color);
			}
		}
	}

	@Nullable
	private Quest drawAchievementIcons(int mouseX, int mouseY, double shiftX, double shiftY) {
		double zoom = this.viewportZoom;

		Quest hoveredAchievment = null;
		for (Quest quest : currentPage.getQuests()) {
			QuestData template = quest.data;
			//boolean secretUndiscovered = isSecretUndiscovered(ach);
			double achViewX = (this.viewportLeft + this.viewportWidth / 2d) + (quest.getX() * ACHIEVEMENT_CELL_WIDTH - shiftX) * zoom;
			double achViewY = (this.viewportTop + this.viewportHeight / 2d) + (quest.getY() * ACHIEVEMENT_CELL_HEIGHT - shiftY) * zoom;
			if (achViewX < this.viewportLeft - ACHIEVEMENT_CELL_WIDTH * zoom || achViewY < this.viewportTop - ACHIEVEMENT_CELL_HEIGHT * zoom || achViewX > this.viewportRight || achViewY > this.viewportBottom) { // Continue if outside viewport
				continue;
			}

			if (quest.isCompleted()) {
				float brightness = 1.0F;
				GLRenderer.setColor4f(brightness, brightness, brightness, 1.0F);
			} else if (quest.preRequisitesCompleted()) {
				// Flicker if can unlock
				float brightness = timeSin(1, 600) >= 0.6 ? 0.6F : 0.8F;
				GLRenderer.setColor4f(brightness, brightness, brightness, 1.0F);
			} else {
				// Darken if not unlock-able
				float brightness = 0.3F;
				GLRenderer.setColor4f(brightness, brightness, brightness, 1.0F);
			}

			drawGuiIconDouble(achViewX - (ACHIEVEMENT_ICON_WIDTH - ACHIEVEMENT_CELL_WIDTH) * zoom, achViewY - (ACHIEVEMENT_ICON_HEIGHT - ACHIEVEMENT_CELL_HEIGHT) * zoom, ACHIEVEMENT_ICON_WIDTH * zoom, ACHIEVEMENT_ICON_HEIGHT * zoom, this.currentPage.getQuestBackground(template));

			if (!quest.preRequisitesCompleted()) {
				float brightness = 0.1F;
				GLRenderer.setColor4f(brightness, brightness, brightness, 1.0F);
			}

			GLRenderer.pushFrame();
			GLRenderer.globalSetLightEnabled(true);
			GLRenderer.enableState(State.CULL_FACE);
			ItemStack achievementItem = template.getIcon().getDefaultStack();

			GLRenderer.modelM4f().translate((float) (achViewX + 3 * zoom), (float) (achViewY + 3 * zoom), 0);
			GLRenderer.modelM4f().scale((float) zoom, (float) zoom, 1);
			//            ItemModelDispatcher.getInstance().getDispatch(achievementItem).renderItemIntoGui(GLRenderer.getTessellator(), mc.font, mc.textureManager, achievementItem.getDefaultStack(), 0, 0, 1.0f);
			ItemModelDispatcher.getInstance().getDispatch(achievementItem).renderGui(GLRenderer.getTessellator(), null, achievementItem, 0, 0, LightIndexHelper.lightIndex2i(15, 15), 1f);

			GLRenderer.globalSetLightEnabled(false);
			GLRenderer.popFrame();

			GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			if ((mouseX >= 0 && mouseY >= this.viewportTop && mouseX < this.width && mouseY < this.viewportBottom) && // In viewport and
				(mouseX >= achViewX && mouseX <= achViewX + 22 * zoom && mouseY >= achViewY && mouseY <= achViewY + 22 * zoom)) { // Hovering over achievement
				hoveredAchievment = quest;
			}
		}
		return hoveredAchievment;
	}

	private void drawAchievementToolTip(Quest quest, int mouseX, int mouseY) {
		StringBuilder s = new StringBuilder(quest.getTranslatedName());
		if (quest.getPreRequisites().isEmpty() || quest.preRequisitesCompleted()) {
			if (quest.isCompleted()) {
				s.append("\n").append(TextFormatting.LIME).append("Completed!");
				if (!quest.areAllRewardsRedeemed()) {
					s.append("\n").append(TextFormatting.LIGHT_BLUE).append("Unclaimed rewards!");
				}
			} else {
				s.append("\n").append(TextFormatting.LIGHT_GRAY).append(quest.numberOfCompletedTasks()).append("/").append(quest.getTasks().size()).append(" tasks.");
			}
		} else {
			s.append("\n").append(TextFormatting.RED).append("Requires ").append("(").append(quest.getQuestLogic()).append("):");
			for (Quest preRequisite : quest.getPreRequisites()) {
				s.append("\n").append(TextFormatting.RED).append("- ").append(preRequisite.getTranslatedName());
			}
			s.append(TextFormatting.WHITE);
		}
		tooltip.render(s.toString(), mouseX, mouseY, 8, -8);
	}

	public boolean drawSidebar() {
		return AchievementPageRegistry.getInstance().getPages().size() > 1;
	}

	private void scrollPagesList(float amount) {
		if (amount == 0.0f) return;

		this.pageListScrollAmount += amount;
		onScrollPagesList();
	}

	private void onScrollPagesList() {
		int totalPagesListHeight = getTotalPagesListHeight();
		if (this.pageListScrollAmount < 0 || this.pagesListScrollRegionHeight > totalPagesListHeight)
			this.pageListScrollAmount = 0;
		else if (this.pageListScrollAmount > totalPagesListHeight - this.pagesListScrollRegionHeight)
			this.pageListScrollAmount = totalPagesListHeight - this.pagesListScrollRegionHeight;
	}

	private int getTotalPagesListHeight() {
		return PAGE_BUTTON_HEIGHT * AchievementPageRegistry.getInstance().getPages().size();
	}

	@Nullable
	private ChapterPage drawPagesListItems(int x, int y, int width, int mouseX, int mouseY) {
		int y2 = y;
		ChapterPage pageHovered = null;
		for (ChapterPage page : chapters) {
			String name = page.getName();
			int textColor = 0xFF7F7F7F;
			if (page == this.currentPage) {
				textColor = 0xFFFFFFFF;
			}
			if (mouseX >= x && mouseX < x + width && mouseY >= y2 && mouseY < y2 + PAGE_BUTTON_HEIGHT) {
				textColor = 0xFFFFFFA0;
				pageHovered = page;
			}
			this.renderItem.render(page.getIcon(), x, y2 + (PAGE_BUTTON_HEIGHT / 2) - 9);
			if (page.getCompletionFraction() >= 1) {
				GLRenderer.setColor4f(1f, 1f, 1f, 1f);
				drawGuiIcon(x + 8, y2 + (PAGE_BUTTON_HEIGHT / 2) - 9 + 8, 11, 11, TextureRegistry.getTexture("minecraft:gui/screen/achievement/star"));
			}
			drawStringShadow(this.fontRenderer, name, x + 19, y2 + (PAGE_BUTTON_HEIGHT / 2) - 4, textColor);
			y2 += PAGE_BUTTON_HEIGHT;
		}

		return pageHovered;
	}

	protected void drawPagesListScrollBar(int mouseX, int mouseY) {
		float totalPagesListHeight = getTotalPagesListHeight();
		float scrollBarHeightPercent = this.pagesListScrollRegionHeight / totalPagesListHeight;

		if (scrollBarHeightPercent > 1.0f) return;

		GLRenderer.pushFrame();
		GLRenderer.setShader(Shaders.COLOR);

		int scrollBarX = this.pageListRight - 6;

		int scrollBarHeightPx = (int) (scrollBarHeightPercent * this.pagesListScrollRegionHeight);
		if (scrollBarHeightPx < 32) {
			scrollBarHeightPx = 32;
		}

		float scrollPercent = this.pageListScrollAmount / (totalPagesListHeight - this.pagesListScrollRegionHeight);

		int scrollBarY = (int) (this.top + (this.pagesListScrollRegionHeight - scrollBarHeightPx) * scrollPercent);

		TessellatorGeneral t = GLRenderer.getTessellator();

		t.startDrawingQuads();
		t.setColorOpaque3i(0, 0, 0);
		t.drawRectangle(scrollBarX, this.top, 6, this.pagesListScrollRegionHeight);
		t.setColor2i(0x808080, 255);
		t.drawRectangle(scrollBarX, scrollBarY, 6, scrollBarHeightPx);
		t.setColor2i(0xc0c0c0, 255);
		t.drawRectangle(scrollBarX + 1, scrollBarY, 5, scrollBarHeightPx - 1);
		t.draw();

		GLRenderer.popFrame();

		if (this.clickX != null && this.clickY != null) {
			if (this.clickX >= scrollBarX && this.clickY >= this.top && this.clickX <= scrollBarX + 6 && this.clickY < this.bottom) {
				if (this.oldPagesListScrollAmount == null) {
					this.oldPagesListScrollAmount = this.pageListScrollAmount;
				}
				this.pageListScrollAmount = this.oldPagesListScrollAmount + (this.clickY - mouseY) * (1.0f / scrollBarHeightPercent) * -1.0f;
				onScrollPagesList();
			}
		} else {
			this.oldPagesListScrollAmount = null;
		}
	}

	private void overlayBackground(int minX, int maxX, int minY, int maxY, int color) {
		TessellatorGeneral tessellator = GLRenderer.getTessellator();
		this.mc.textureManager.loadTexture("/assets/minecraft/textures/gui/background.png").bind();
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float scale = 32F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque1i(color);
		tessellator.addVertexWithUV(minX, maxY, 0.0D, (float) minX / scale, (float) maxY / scale);
		tessellator.addVertexWithUV(maxX, maxY, 0.0D, (float) maxX / scale, (float) maxY / scale);
		tessellator.setColorOpaque1i(color);
		tessellator.addVertexWithUV(maxX, minY, 0.0D, (float) maxX / scale, (float) minY / scale);
		tessellator.addVertexWithUV(minX, minY, 0.0D, (float) minX / scale, (float) minY / scale);
		tessellator.draw();
	}

	public ChapterPage getCurrentPage() {
		return currentPage;
	}

	public static class BGLayer {
		private IconCoordinate[] data;
		private int width;
		private int height;
		public final int id;

		public BGLayer(int id) {
			this.id = id;
			this.data = new IconCoordinate[0];
			this.width = 0;
			this.height = 0;
		}

		public int getWidth() {
			return this.width;
		}

		public int getHeight() {
			return this.height;
		}

		public IconCoordinate[] getData() {
			return this.data;
		}

		protected void resize(int width, int height) {
			this.width = width;
			this.height = height;
			this.data = new IconCoordinate[width * height];
		}

		public void put(IconCoordinate coordinate, int x, int y) {
			if (x < 0) return;
			if (y < 0) return;
			if (x >= this.width) return;
			if (y >= this.height) return;
			this.data[makeIndex(x, y)] = coordinate;
		}

		public IconCoordinate get(int x, int y) {
			if (x < 0) return null;
			if (y < 0) return null;
			if (x >= this.width) return null;
			if (y >= this.height) return null;
			return this.data[makeIndex(x, y)];
		}

		private int makeIndex(int x, int y) {
			return x % this.width + y * this.width;
		}
	}
}
