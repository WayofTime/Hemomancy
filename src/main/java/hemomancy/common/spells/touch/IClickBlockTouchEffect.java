package hemomancy.common.spells.touch;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IClickBlockTouchEffect 
{
	public boolean clickBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit);
	
	public float getPotency();
}
