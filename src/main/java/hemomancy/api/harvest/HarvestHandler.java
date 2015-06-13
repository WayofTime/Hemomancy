package hemomancy.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class HarvestHandler 
{
	public abstract boolean harvestBlock(World world, Block block, IBlockState state, BlockPos pos);
}
