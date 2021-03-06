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
		int width = this.getWidthForPotency(potency);
		int height = this.getHeightForPotency(potency);
		int depth = this.getDepthForPotency(potency);
		
		int harvestLevel = 2;
		
		boolean success = false;
		
		for(int i = 0; i <= depth; i++)
		{
			for(int j = -height; j <= height; j++)
			{
				for(int k = -width; k <= width; k++)
				{
					//This is "rotation" logic to rotate the block based on the facing hit
					BlockPos newPos = pos.add(sideHit.getFrontOffsetX() != 0 ? (-i * sideHit.getFrontOffsetX()) : (sideHit.getFrontOffsetZ() != 0 ? k : j), sideHit.getFrontOffsetY() != 0 ? -i * sideHit.getFrontOffsetY() : j, sideHit.getFrontOffsetZ() != 0 ? (-i * sideHit.getFrontOffsetZ()) : (k));
					IBlockState newState = world.getBlockState(newPos);
					
					if(Utils.digBlock(world, newPos, newState, harvestLevel, 0, false))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	public int getWidthForPotency(float potency)
	{
		return 1;
	}
	
	public int getDepthForPotency(float potency)
	{
		return 4;
	}
	
	public int getHeightForPotency(float potency)
	{
		return 2;
	}

	@Override
	public float getPotency()
	{
		return this.potency;
	}
}
