package hemomancy.common.spells.beam;

import hemomancy.common.util.Utils;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBreakBlockBeamEffect implements IBlockBeamEffect
{
	static HashMap<String, BlockPos> lastPosMap = new HashMap();
	static HashMap<String, Float> breakCount = new HashMap();
	
	public float potency;
	
	public BlockBreakBlockBeamEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean collideWithBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{
		float hardness = block.getBlockHardness(world, pos);
		if(hardness >= 0)
		{
			String pp = "R" + player.getName();
	        if (!player.worldObj.isRemote) 
	        {
	        	pp = "S" + player.getName();
	        }
	        
			float speed = 0.25f; //TODO Material checks for the block
			int harvestLevel = 2;
			int fortune = 0;
			boolean silkHarvest = false;
			
			if(pos.equals(lastPosMap.get(pp)))
			{
				float breakProgress = breakCount.get(pp);
				
				if(!world.isRemote)
				{
					breakProgress += speed;
					
					if(breakProgress >= hardness && Utils.digBlock(world, pos, state, harvestLevel, fortune, silkHarvest))
					{
						breakCount.put(pp, 0.0f);
						lastPosMap.put(pp, null);
						
						return true;
					}else
					{
						breakCount.put(pp, breakProgress);
					}
				}
			}else
			{
				lastPosMap.put(pp, pos);
				breakCount.put(pp, 0.0f);
			}
		}
		
		return false;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
