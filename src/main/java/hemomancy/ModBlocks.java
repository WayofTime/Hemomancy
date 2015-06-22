package hemomancy;

import hemomancy.common.blocks.BlockFlammableGas;
import hemomancy.common.blocks.BlockPlasma;
import hemomancy.common.blocks.BlockSpellTinkerer;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks 
{
	public static Block blockSpellTinkerer;
	public static Block blockPlasma;
	public static Block blockFlammableGas;
	
	public static void init()
    {
		blockSpellTinkerer = new BlockSpellTinkerer();
		blockPlasma = new BlockPlasma();
		blockFlammableGas = new BlockFlammableGas();
    }

    public static void registerBlocks()
    {
    	GameRegistry.registerBlock(blockSpellTinkerer, "blockSpellTinkerer");
    	GameRegistry.registerBlock(blockPlasma, "blockPlasma");
    	GameRegistry.registerBlock(blockFlammableGas, "blockFlammableGas");
    }
}
