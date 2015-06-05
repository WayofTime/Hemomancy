package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.DamageCounterExtendedProperties;
import hemomancy.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class IceProjectileEffect implements IOnProjectileCollideEffect
{
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity, float potency) 
	{
		boolean bool1 = DamageCounterExtendedProperties.get(hitEntity).addToIceCounter(1, 3);
		boolean bool2 = this.createIceAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY + hitEntity.getEyeHeight()/2.0, hitEntity.posZ, potency);
		return bool1 || bool2;
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		return this.createIceAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		return this.createIceAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency / 3.0f);
	}
	
	private boolean createIceAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		int xPos = (int) Math.floor(x);
		int yPos = (int) Math.floor(y);
		int zPos = (int) Math.floor(z);
		
		int radius = getRadiusOfIce(potency);
		
		return Utils.freezeBlocksInSphere(projectile.worldObj, new BlockPos(xPos, yPos, zPos), radius, 0.30f, 1);
	}
	
	private int getRadiusOfIce(float potency)
	{
		return 2;
	}
}
