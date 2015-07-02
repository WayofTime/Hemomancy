package hemomancy.common.spells.summon;

import hemomancy.api.spells.summon.ISummonBlockManipulator;
import hemomancy.common.entity.mob.EntitySummon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WoodCutterBlockManipulator implements ISummonBlockManipulator
{
	public float potency;
	public static String key = "woodCutter";
	
	public WoodCutterBlockManipulator(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean canManipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		return block instanceof BlockLog;
	}

	@Override
	public boolean manipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		if(summon.targetKey != key && canManipulateBlock(summon, world, pos, block, state))
		{
			summon.targetKey = key;
			summon.targetPos = pos;
			
			return true;
		}
		
		return false;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
