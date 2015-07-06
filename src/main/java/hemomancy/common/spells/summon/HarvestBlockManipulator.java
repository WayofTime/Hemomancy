package hemomancy.common.spells.summon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import hemomancy.api.harvest.HarvestRegistry;
import hemomancy.api.spells.summon.ISummonBlockManipulator;
import hemomancy.common.entity.mob.EntitySummon;

public class HarvestBlockManipulator implements ISummonBlockManipulator
{
	public float potency;
	
	public HarvestBlockManipulator(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean canManipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		return HarvestRegistry.canHarvestBlock(world, block, state, pos);
	}

	@Override
	public boolean manipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state) 
	{
		return HarvestRegistry.harvestBlock(world, block, state, pos, false);
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
