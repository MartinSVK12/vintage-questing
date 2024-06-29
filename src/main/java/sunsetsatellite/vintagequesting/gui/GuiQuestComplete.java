package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.item.Item;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.vintagequesting.quest.Quest;

public class GuiQuestComplete extends Gui
{
    private Minecraft theGame;
    private int achievementWindowWidth;
    private int achievementWindowHeight;
    private String name;
    private String desc;
    private Quest theQuest;
    private long startTime;
    private boolean isPermanent;

    public GuiQuestComplete(Minecraft minecraft)
    {
        theGame = minecraft;
    }

    public boolean isDisplayingQuest(Quest quest)
    {
        return (theQuest == quest) && (startTime != 0L);
    }

    public void queueQuestComplete(Quest quest)
    {
        name = I18n.getInstance().translateKey("gui.vq.quest.label.complete");
        desc = quest.getTranslatedName();
        startTime = System.currentTimeMillis();
		theQuest = quest;
        isPermanent = false;
    }

    public void queueQuestInformation(Quest quest)
    {
        name = quest.getTranslatedName();
        desc = quest.numberOfCompletedTasks()+"/"+quest.getTasks().size()+" tasks.";
        if (theQuest == null)
        {
            startTime = System.currentTimeMillis() - 2500L;
        }
        else
        {
            startTime = System.currentTimeMillis();
        }
		theQuest = quest;
        isPermanent = true;
    }

    private void updateQuestCompleteWindowScale()
    {
        GL11.glViewport(0, 0, theGame.resolution.width, theGame.resolution.height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        achievementWindowWidth = theGame.resolution.scaledWidth;
        achievementWindowHeight = theGame.resolution.scaledHeight;
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, achievementWindowWidth, achievementWindowHeight, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }

    public void updateQuestCompleteWindow()
    {
        if(Minecraft.hasPaidCheckTime > 0L)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            Lighting.disable();
			updateQuestCompleteWindowScale();
            String s = I18n.getInstance().translateKeyAndFormat("gui.achievement.label.unlicensed.1", Global.VERSION);
            String s1 = I18n.getInstance().translateKey("gui.achievement.label.unlicensed.2");
            String s2 = I18n.getInstance().translateKey("gui.achievement.label.unlicensed.3");
            theGame.fontRenderer.drawStringWithShadow(s, 2, 2, 0xffffff);
            theGame.fontRenderer.drawStringWithShadow(s1, 2, 11, 0xffffff);
            theGame.fontRenderer.drawStringWithShadow(s2, 2, 20, 0xffffff);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        if(theQuest == null || startTime == 0L || theGame.thePlayer == null)
        {
            return;
        }
        double animProgress = (double)(System.currentTimeMillis() - startTime) / 3000D;
        if (isPermanent && animProgress > 0.5) animProgress = 0.5;
        if(!isPermanent && (animProgress < 0.0D || animProgress > 1.0D))
        {
            startTime = 0L;
            return;
        }
		updateQuestCompleteWindowScale();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        double d1 = animProgress * 2D;
        if(d1 > 1.0D)
        {
            d1 = 2D - d1;
        }
        d1 *= 4D;
        d1 = 1.0D - d1;
        if(d1 < 0.0D)
        {
            d1 = 0.0D;
        }
        d1 *= d1;
        d1 *= d1;
        int i = achievementWindowWidth - 160;
        int j = (int) (-d1 * 36D);
        int k = theGame.renderEngine.getTexture("/assets/minecraft/textures/gui/achievement/bg.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawTexturedModalRect(i, j, 96, 202, 160, 32);
        if(isPermanent)
        {
            theGame.fontRenderer.func_27278_a(desc, i + 30, j + 7, 120, -1);
        } else
        {
            theGame.fontRenderer.drawString(name, i + 30, j + 7, -256);
            theGame.fontRenderer.drawString(desc, i + 30, j + 18, -1);
        }
        GL11.glDepthMask(true);
        Lighting.enableInventoryLight();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Item achievementItem = theQuest.getIcon().asItem();
        ItemModelDispatcher.getInstance().getDispatch(achievementItem).renderItemIntoGui(Tessellator.instance, theGame.fontRenderer, theGame.renderEngine, achievementItem.getDefaultStack(), i + 8, j + 8, 1.0f);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
