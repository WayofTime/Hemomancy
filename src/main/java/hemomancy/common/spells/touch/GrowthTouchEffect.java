package hemomancy.common.spells.touch;

import hemomancy.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class GrowthTouchEffect implements IClickBlockTouchEffect
{
	public float potency;
	
	public GrowthTouchEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean clickBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{				
		return growPlantsAtPoint(world, pos, potency);
	}

	private boolean growPlantsAtPoint(World world, BlockPos pos, float potency)
	{		
		int radius = this.getRadiusOfGrowth(potency);
		float chance = this.getChanceOfGrowth(potency);
		
		boolean success = false;
		
		for(int i = -radius; i <= radius; i++)
		{
			for(int j = -radius; j <= radius; j++)
			{
				for(int k = -radius; k <= radius; k++)
				{
					if(world.rand.nextFloat() < chance && Utils.growPlantAtBlock(world, pos.add(i, j, k)))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	private int getRadiusOfGrowth(float potency)
	{
		return (int)(potency * potency);
	}
	
	private float getChanceOfGrowth(float potency)
	{
		return potency * 0.5f;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
