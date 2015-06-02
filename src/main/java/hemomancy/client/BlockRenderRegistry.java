package hemomancy.client;

import hemomancy.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

public class BlockRenderRegistry 
{
	public static void registerBlockRenderers()
	{
		registerBlock(ModBlocks.blockSpellTinkerer);
	}
	
	public static void registerBlock(Block block) 
	{
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("hemomancy" + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
