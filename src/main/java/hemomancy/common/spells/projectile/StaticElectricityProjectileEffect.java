package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class StaticElectricityProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public StaticElectricityProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		if(hitEntity.hurtResistantTime > 0)
		{
			hitEntity.hurtResistantTime = Math.max(0, hitEntity.hurtResistantTime - getHurtDiscountForPotency(potency));
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return false;
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return false;
	}
	
	public int getHurtDiscountForPotency(float potency)
	{
		return 5;
	}
	
	@Override
	public float getPotency() 
	{
		return this.potency;
	}
}
