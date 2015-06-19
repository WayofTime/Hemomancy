package hemomancy.common.spells.touch;

import hemomancy.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class HarvestTouchEffect implements IClickBlockTouchEffect
{
	public float potency;
	
	public HarvestTouchEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean clickBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{				
		return harvestPlantsAtPoint(world, pos, potency);
	}
	
	private boolean harvestPlantsAtPoint(World world, BlockPos pos, float potency)
	{		
		int radius = this.getRadiusOfHarvest(potency);
		
		boolean success = false;
		
		for(int i = -radius; i <= radius; i++)
		{
			for(int j = -radius; j <= radius; j++)
			{
				for(int k = -radius; k <= radius; k++)
				{
					if(Utils.harvestPlantAtBlock(world, pos.add(i, j, k), false))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	private int getRadiusOfHarvest(float potency)
	{
		return (int)(potency * potency);
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
