package hemomancy.common.spells.touch;

import hemomancy.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBreakTouchEffect implements IClickBlockTouchEffect
{
	public float potency;
	
	public BlockBreakTouchEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean clickBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{
		return breakBlocksAtLocation(world, pos, state, sideHit, potency);
	}

	public boolean breakBlocksAtLocation(World world, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency)
	{
		int width = this.getWidthForPotency(potency);
		int height = this.getHeightForPotency(potency);
		int depth = this.getDepthForPotency(potency);
		
		int harvestLevel = 2;
		
		boolean success = false;
		
		for(int i = 0; i <= depth; i++)
		{
			for(int j = -height; j <= height; j++)
			{
				for(int k = -width; k <= width; k++)
				{
					//This is "rotation" logic to rotate the block based on the facing hit
					BlockPos newPos = pos.add(sideHit.getFrontOffsetX() != 0 ? (-i * sideHit.getFrontOffsetX()) : (sideHit.getFrontOffsetZ() != 0 ? k : j), sideHit.getFrontOffsetY() != 0 ? -i * sideHit.getFrontOffsetY() : j, sideHit.getFrontOffsetZ() != 0 ? (-i * sideHit.getFrontOffsetZ()) : (k));
					IBlockState newState = world.getBlockState(newPos);
					
					if(Utils.digBlock(world, newPos, newState, harvestLevel, 0, false))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	public int getWidthForPotency(float potency)
	{
		return 1;
	}
	
	public int getDepthForPotency(float potency)
	{
		return 4;
	}
	
	public int getHeightForPotency(float potency)
	{
		return 2;
	}
	
	@Override
	public float getPotency() 
	{
		return potency;
	}
}
