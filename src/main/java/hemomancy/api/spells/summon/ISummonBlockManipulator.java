package hemomancy.api.spells.summon;

import hemomancy.common.entity.mob.EntitySummon;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ISummonBlockManipulator 
{
	public boolean canManipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state);
	
	public boolean manipulateBlock(EntitySummon summon, World world, BlockPos pos, Block block, IBlockState state);
	
	public float getPotency();
}
