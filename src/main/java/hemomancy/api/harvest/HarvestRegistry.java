package hemomancy.api.harvest;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class HarvestRegistry 
{
	public static List<HarvestHandler> harvestHandlerList = new LinkedList();
	
	public static void registerHarvestHandler(HarvestHandler handler)
	{
		harvestHandlerList.add(handler);
	}
	
	public static boolean harvestBlock(World world, Block block, IBlockState state, BlockPos pos)
	{
		for(HarvestHandler handler : harvestHandlerList)
		{
			if(handler.harvestBlock(world, block, state, pos))
			{
				return true;
			}
		}
		
		return false;
	}
}