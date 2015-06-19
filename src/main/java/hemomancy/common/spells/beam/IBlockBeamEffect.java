package hemomancy.common.spells.beam;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IBlockBeamEffect 
{
	public boolean collideWithBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit);
	
	public float getPotency();
}
