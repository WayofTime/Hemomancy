package hemomancy.common.spells.beam;

import hemomancy.api.spells.beam.IBlockBeamEffect;
import hemomancy.common.util.Utils;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class IceBlockBeamEffect implements IBlockBeamEffect
{
	static HashMap<String, BlockPos> lastPosMap = new HashMap();
	static HashMap<String, Float> breakCount = new HashMap();
	
	public float potency;
	
	public IceBlockBeamEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean collideWithBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{
		return !world.isRemote && Utils.freezeBlock(world, pos);
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
