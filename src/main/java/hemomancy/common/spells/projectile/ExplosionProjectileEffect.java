package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.api.spells.projectile.IOnProjectileUpdateEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class ExplosionProjectileEffect implements IOnProjectileUpdateEffect, IOnProjectileCollideEffect
{
	@Override
	public boolean onProjectileUpdate(Entity projectile, EntityPlayer shooter, float potency)
	{
		return false;
	}

	@Override
	public boolean onProjectileStickyUpdate(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, int ticksInGround, float potency) 
	{
		if(ticksInGround >= 100)
		{
			this.createExplosionAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency);
			projectile.setDead();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity, float potency) 
	{
		return this.createExplosionAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY + hitEntity.getEyeHeight()/2.0, hitEntity.posZ, potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		return this.createExplosionAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		return this.createExplosionAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency / 3.0f);
	}
	
	private boolean createExplosionAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		projectile.worldObj.createExplosion(shooter, x, y, z, 5 * potency, false);
		return true;
	}
}
