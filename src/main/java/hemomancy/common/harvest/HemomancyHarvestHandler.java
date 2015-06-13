package hemomancy.common.harvest;

import hemomancy.api.harvest.HarvestHandler;
import hemomancy.common.util.Utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class HemomancyHarvestHandler extends HarvestHandler
{
	public boolean canHarvestBlock(Block block, IBlockState state)
	{
		int meta = block.getMetaFromState(state);
		
		if(meta >= 7)
		{
			return block == Blocks.wheat;
		}
		
		return false;
	}
		
	@Override
	public boolean harvestBlock(World world, Block block, IBlockState state, BlockPos pos) 
	{
		if(canHarvestBlock(block, state))
		{
			IPlantable seed = getItemSeedFromBlock(block);
			List<ItemStack> dropList = block.getDrops(world, pos, state, 0);
			boolean hasTakenSeed = true;
			
			if(seed != null)
			{
				hasTakenSeed = false;
				
				for(ItemStack stack : dropList)
				{
					if(stack != null)
					{
						Item droppedItem = stack.getItem();
						if(droppedItem == seed)
						{
							hasTakenSeed = true;
							stack.stackSize--;
							
							if(stack.stackSize <= 0)
							{
								dropList.remove(stack);
							}
							
							break;
						}
					}
				}
			}
			
			if(hasTakenSeed)
			{
				block.breakBlock(world, pos, state);
				
				if(seed != null)
				{
					IBlockState newState = seed.getPlant(world, pos);
					world.setBlockState(pos, newState, 3);
				}
				
				Utils.dropItemsAtPosition(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropList);
				
				return true;
			}
		}
		
		return false;
	}
	
	public IPlantable getItemSeedFromBlock(Block block)
	{
		if(block == Blocks.wheat)
		{
			return (IPlantable)Items.wheat_seeds;
		}
		
		return null;
	}
}
