package hemomancy.client;

import hemomancy.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class ItemRenderRegistry 
{
	public static void registerItemRenderers()
	{
		registerItem(ModItems.itemAmpoule);
	}
	
	public static void registerItem(Item item) 
	{
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("hemomancy" + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
