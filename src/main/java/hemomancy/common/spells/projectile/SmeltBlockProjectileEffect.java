package hemomancy.common.spells.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.Utils;

public class SmeltBlockProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public SmeltBlockProjectileEffect(float potency)
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
		return smeltBlocksAtPoint(projectile, shooter, pos.getX(), pos.getY(), pos.getZ(), potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return smeltBlocksAtPoint(projectile, shooter, pos.getX(), pos.getY(), pos.getZ(), potency / 2.0f);
	}

	public boolean smeltBlocksAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		int xPos = (int) Math.floor(x);
		int yPos = (int) Math.floor(y);
		int zPos = (int) Math.floor(z);
		
		int radius = getRadiusOfSmelting(potency);
		
		return Utils.smeltBlocksInSphere(projectile.worldObj, new BlockPos(xPos, yPos, zPos), radius, 0.30f, 0, true);
	}
	
	private int getRadiusOfSmelting(float potency)
	{
		return 2;
	}
	
	@Override
	public float getPotency() 
	{
		return potency;
	}
}
