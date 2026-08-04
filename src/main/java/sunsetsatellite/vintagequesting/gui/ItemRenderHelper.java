package sunsetsatellite.vintagequesting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.LightIndexHelper;
import org.jetbrains.annotations.Nullable;

public class ItemRenderHelper {
	public static Minecraft mc;

	private static void initMC() {
		if (mc == null) {
			mc = Minecraft.getMinecraft();
		}
	}

	public static void renderItemStack(@Nullable ItemStack itemStack, int x, int y, double scaleX, double scaleY) {
		initMC();
		GLRenderer.pushFrame();
		Lighting.enableInventoryLight();
		GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GLRenderer.enableState(State.DEPTH_TEST);
		if (itemStack != null) {
			ItemModel itemModel = ItemModelDispatcher.getInstance().getDispatch(itemStack.getItem());
			GLRenderer.modelM4f().translate(x, y, 0);
			GLRenderer.modelM4f().scale((float) scaleX, (float) scaleY, 1);
			itemModel.renderGui(GLRenderer.getTessellator(), null, itemStack, 0, 0, LightIndexHelper.lightIndex2f(15, 15), 1);
		}


		Lighting.disable();
		GLRenderer.disableState(State.DEPTH_TEST);
		GLRenderer.popFrame();
	}
}
