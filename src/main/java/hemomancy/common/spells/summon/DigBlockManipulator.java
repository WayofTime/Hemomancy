package hemomancy.common.spells.summon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import hemomancy.api.spells.summon.ISummonBlockManipulator;
import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.util.Utils;

public class DigBlockManipulator implements ISummonBlockManipulator
{
	public float potency;
	
	public DigBlockManipulator(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean canManipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		System.out.println("Called?");
		return !world.isAirBlock(pos) && !block.isTranslucent();
	}

	@Override
	public boolean manipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		return Utils.digBlock(world, pos, state, 100, 0, false);
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
