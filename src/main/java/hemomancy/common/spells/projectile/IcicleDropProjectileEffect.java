package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.Utils;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class IcicleDropProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public IcicleDropProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return this.createIceAtPoint(projectile, shooter, hitEntity.getPosition(), potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.createIceAtPoint(projectile, shooter, pos, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.createIceAtPoint(projectile, shooter, pos, potency / 3.0f);
	}
	
	private boolean createIceAtPoint(Entity projectile, EntityPlayer shooter, BlockPos pos, float potency)
	{		
		World world = projectile.worldObj;
		
		if (world.isRemote)
		{
			return false;
		}
		
		int height = 7;
		float radius = 2.5f;
		
		int offset = 7;
		
		BlockPos translatedPos = pos.add(0, height + offset, 0);
		
		List<BlockPos> spikePosList = Utils.getPillarBlocksForPoint(translatedPos, EnumFacing.DOWN, height, radius, 0.3f, 0.5f);
		
		boolean success = false;
		
		for(BlockPos newPos : spikePosList)
		{
			if(world.isAirBlock(newPos))
			{
				world.setBlockState(newPos, Blocks.ice.getDefaultState());
	            EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)newPos.getX() + 0.5D, (double)newPos.getY() + 0.05, (double)newPos.getZ() + 0.5D, world.getBlockState(newPos));
	            world.spawnEntityInWorld(entityfallingblock);
	            success = true;
			}
		}		
		
		return success;
	}

	@Override
	public float getPotency() 
	{
		return this.potency;
	}
}
