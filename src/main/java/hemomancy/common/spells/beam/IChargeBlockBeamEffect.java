package hemomancy.common.spells.beam;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IChargeBlockBeamEffect 
{
	public boolean releasePowerIntoBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit, int chargeTime);
	
	public float getPotency();
}
