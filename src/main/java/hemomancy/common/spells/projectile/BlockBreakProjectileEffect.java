package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBreakProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public BlockBreakProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return false;
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return breakBlocksAtLocation(projectile.worldObj, pos, state, sideHit, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return breakBlocksAtLocation(projectile.worldObj, pos, state, sideHit, potency / 2.0f);
	}
	
	public boolean breakBlocksAtLocation(World world, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency)
	{
		switch(sideHit)
		{
		case DOWN:
			
			break;
			
		case UP:
			
			break;
			
		case EAST:
			
			break;
			
		case NORTH:
			
			break;
			
		case SOUTH:
			
			break;
			
		case WEST:
			
			break;
		}
		return Utils.digBlock(world, pos, state, 0, false);
	}
	
	public int getWidthForPotency(float potency)
	{
		return 0;
	}
	
	public int getDepthForPotency(float potency)
	{
		return 1;
	}
	
	public int getHeightForPotency(float potency)
	{
		return 1;
	}

	@Override
	public float getPotency()
	{
		return this.potency;
	}
}
