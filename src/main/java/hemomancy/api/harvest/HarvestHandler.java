package hemomancy.api.harvest;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class HarvestHandler 
{
	/**
	 * Called by the HarvestRegistry to break the block of the crop at the location. The mod needs to check if the block is first a valid crop to it.
	 * Items should be dropped in the world and the crop should be reset. See: replantSeed.
	 * 
	 * @param world
	 * @param block
	 * @param state
	 * @param pos			Location of the crop
	 * @param replantSeed 	If true, the handler will replant a seed taken from the drops of the crop at the same position. This should not do anything
	 * 						special for crops that don't have seeds. If false, it will instead just break the block.
	 * @return				true if the block was successfully harvested, false if not.
	 */
	public abstract boolean harvestBlock(World world, Block block, IBlockState state, BlockPos pos, boolean replantSeed);
	
	public abstract boolean canHarvestBlock(World world,  Block block, IBlockState state, BlockPos pos);
}
