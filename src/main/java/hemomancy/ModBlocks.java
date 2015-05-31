package hemomancy;

import hemomancy.common.blocks.BlockSpellTinkerer;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks 
{
	public static Block blockSpellTinkerer;
	
	public static void init()
    {
		blockSpellTinkerer = new BlockSpellTinkerer();
    }

    public static void registerBlocks()
    {
    	GameRegistry.registerBlock(blockSpellTinkerer, "blockSpellTinkerer");
    }
}
