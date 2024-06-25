package sunsetsatellite.vintagequesting.gui.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Scissor;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.vintagequesting.interfaces.IClickable;
import sunsetsatellite.vintagequesting.interfaces.IRenderable;

import java.util.ArrayList;
import java.util.List;

public class GuiVerticalContainer
    extends Gui
{
    private final Minecraft mc;
    private float scrollAmount = 0;
    public final List<IRenderable> renderables = new ArrayList<>();
    private final int height;

	private int width;
    private int scrollbarX;
    private int scrollbarY;
    private int scrollbarWidth;
    private int scrollbarHeight;
    private boolean isScrolling = false;
    private int clickY;
    private float previousScrollAmount = 0.0f;
	private int lastX;
	private int lastY;
	private final int elementSpacing;

    public GuiVerticalContainer(int width, int height, int elementSpacing)
    {
        this.width = width;
		this.elementSpacing = elementSpacing;
        this.mc = Minecraft.getMinecraft(this);
        this.height = height;
    }

    public int getHeight()
    {
        return height;
    }

	public int getWidth() {
		return width;
	}

	private void scroll(float amount)
    {
        scrollAmount = MathHelper.clamp(scrollAmount + amount, 0.0f, 1.0f);
    }

    public void render(int x, int y, int mouseX, int mouseY)
    {
		this.lastX = x;
		this.lastY = y;


        // Do scroll
        if (mouseInRegion(x, y, mouseX, mouseY))
        {
            float wheel = Mouse.getDWheel();
            if (wheel != 0.0f)
                scroll(wheel / -1200.0f);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //drawBackground(x, y);
        drawScrollbar(x, y, mouseX, mouseY);

        Scissor.enable(x + 1, y + 1, getWidth() - 2, getHeight() - 2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Draw lines
		int height = 0;
		for (int i = 0; i < renderables.size(); i++) {
			IRenderable renderable = renderables.get(i);
			renderable.render(x + 4, (y + 4) + height + 4 - getScrollPixels(), mouseX, mouseY);
			height += renderable.getHeight() + elementSpacing;
		}

        Scissor.disable();
    }

    private boolean mouseInRegion(int x, int y, int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX < x + getWidth() && mouseY >= y && mouseY < y + height;
    }

    private int getScrollPixels()
    {
        return (int)(scrollAmount * (getScrollableHeight() - (height - 2)));
    }

    private int getScrollableHeight()
    {
        return Math.max(20 * renderables.size(),height);
    }


    public void onClick(int x, int y, int button)
    {
        if(button == 0 && x >= scrollbarX && x < scrollbarX + scrollbarWidth && y >= scrollbarY && y < scrollbarY + scrollbarHeight) {
        	isScrolling = true;
        	previousScrollAmount = scrollAmount;
        	clickY = y;
        	return;
        }
    }

    public void mouseMovedOrUp(int x, int y, int button)
    {
    	if(button == 0) {
    		isScrolling = false;
    		previousScrollAmount = 0.0f;
    		clickY = 0;
    	}
    }

    private void drawBackground(int x, int y)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xA0A0A0);
        tessellator.addVertex(x, y + height, 0.0D);
        tessellator.addVertex(x + getWidth(), y + height, 0.0D);
        tessellator.addVertex(x + getWidth(), y, 0.0D);
        tessellator.addVertex(x, y, 0.0D);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x000000);
        tessellator.addVertex(x + 1, y + height - 1, 0.0D);
        tessellator.addVertex(x + getWidth() - 1, y + height - 1, 0.0D);
        tessellator.addVertex(x + getWidth() - 1, y + 1, 0.0D);
        tessellator.addVertex(x + 1, y + 1, 0.0D);
        tessellator.draw();
    }

    private void drawScrollbar(int x, int y, int mouseX, int mouseY)
    {
    	int scrollableHeight = getScrollableHeight();
        int displayRegionHeight = height - 2;
        float scrollbarScale = (float) displayRegionHeight / scrollableHeight;

    	scrollbarWidth = 6;
        scrollbarHeight = (int) (scrollbarScale * displayRegionHeight);

        int minScrollbarY = 0;
        int maxScrollbarY = displayRegionHeight - scrollbarHeight;

        int scrollbarDelta = maxScrollbarY - minScrollbarY;

        scrollbarY = y + 1 + (int) (scrollAmount * scrollbarDelta);
        scrollbarX = x + getWidth() - 1 - 6;

        Tessellator t = Tessellator.instance;

        t.startDrawingQuads();
        t.setColorRGBA_I(0x808080, 255);
        t.drawRectangle(scrollbarX, scrollbarY, 6, scrollbarHeight);
        t.setColorRGBA_I(0xc0c0c0, 255);
        t.drawRectangle(scrollbarX, scrollbarY, 5, scrollbarHeight - 1);
        t.draw();

        if(isScrolling) {
        	int delta = mouseY - clickY;
        	float scrolledScreens = delta / (float) scrollbarHeight;
        	float scrolledPixels = displayRegionHeight * scrolledScreens;
        	float scrolledAmount = scrolledPixels / (float) (scrollableHeight - displayRegionHeight);

        	scrollAmount = MathHelper.clamp(previousScrollAmount + scrolledAmount, 0.0f, 1.0f);
        }

    }

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			for (int i = 0; i < renderables.size(); i++) {
				IRenderable renderable = renderables.get(i);
				if (renderable instanceof IClickable) {
					IClickable clickable = (IClickable) renderable;
					int elementX = lastX + 4;
					int elementY = (lastY + 4) + (i * (renderable.getHeight() + 4)) - getScrollPixels();
					if (clickable.mouseClicked(mc, elementX, elementY, mouseX, mouseY)) {
						clickable.click();
					}
				}
			}
		}
	}
}
