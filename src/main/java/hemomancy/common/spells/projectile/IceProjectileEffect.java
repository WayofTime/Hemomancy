package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
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
	public void onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity, float potency) 
	{
		this.createIceAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY + hitEntity.getEyeHeight()/2.0, hitEntity.posZ, potency);
	}

	@Override
	public void onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		this.createIceAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency);
	}

	@Override
	public void onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency) 
	{
		this.createIceAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency / 3.0f);
	}
	
	private void createIceAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		int xPos = (int) Math.floor(x);
		int yPos = (int) Math.floor(y);
		int zPos = (int) Math.floor(z);
		
		int radius = getRadiusOfIce(potency);
		
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					BlockPos pos = new BlockPos(xPos + i, yPos + j, zPos + k);
					Utils.freezeBlock(projectile.worldObj, pos);
				}
			}
		}
	}
	
	private int getRadiusOfIce(float potency)
	{
		return 2;
	}
}
