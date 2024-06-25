package sunsetsatellite.vintagequesting.gui.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Scissor;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiMessageBox
    extends Gui
{
    private final Minecraft minecraft;
    private float scrollAmount = 0;
    private final List<String> lines = new ArrayList<>();
    private final int height;

	private int width;
    private int scrollbarX;
    private int scrollbarY;
    private int scrollbarWidth;
    private int scrollbarHeight;
    private boolean isScrolling = false;
    private int clickY;
    private float previousScrollAmount = 0.0f;

    public GuiMessageBox(int width, int height, String text, int chars)
    {
        this.width = width;

        this.minecraft = Minecraft.getMinecraft(this);
        this.height = height;

        String currentFormat = TextFormatting.WHITE.toString();

        String[] newlines = text.split("\\n",Integer.MAX_VALUE);

        List<String> split = new ArrayList<>();

        for (String newline : newlines) {
            if(newline.isEmpty()){
                split.add("");
                continue;
            }
            String nextFormat = currentFormat;
            int start = 0;
            while (start < newline.length()) {
                String s = newline.substring(start, Math.min(newline.length(), start + chars));
                char[] charArray = s.toCharArray();
                for (int i = 0; i < charArray.length; i++) {
                    char c = charArray[i];
                    if (c == '\247' && i + 1 < s.length()) {
                        nextFormat = "§"+charArray[i+1];
                    }
                }
                split.add(currentFormat+s);
                start += chars;
                currentFormat = nextFormat;
            }
        }

        lines.addAll(split);
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

        // Do scroll
        if (mouseInRegion(x, y, mouseX, mouseY))
        {
            float wheel = Mouse.getDWheel();
            if (wheel != 0.0f)
                scroll(wheel / -1200.0f);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        drawBackground(x, y);
        drawScrollbar(x, y, mouseX, mouseY);

        Scissor.enable(x + 1, y + 1, getWidth() - 2, getHeight() - 2);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        // Draw lines
        for (int i = 0; i < lines.size(); i++)
        {
            minecraft.fontRenderer.drawStringWithShadow(lines.get(i),x + 4,(y + 4) + (i * 12) - getScrollPixels(),0xFFFFFFFF);
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
        return Math.max(20 * lines.size(),height);
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
}
