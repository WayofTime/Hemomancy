package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class GrowthProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public GrowthProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return this.growPlantsAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY, hitEntity.posZ, potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.growPlantsAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.growPlantsAtPoint(projectile, shooter, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, potency / 2.0f);
	}
	
	private boolean growPlantsAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		int xPos = (int)x;
		int yPos = (int)y;
		int zPos = (int)z;
		
		BlockPos pos = new BlockPos(xPos, yPos, zPos);
		
		int radius = this.getRadiusOfGrowth(potency);
		float chance = this.getChanceOfGrowth(potency);
		
		boolean success = false;
		
		for(int i = -radius; i <= radius; i++)
		{
			for(int j = -radius; j <= radius; j++)
			{
				for(int k = -radius; k <= radius; k++)
				{
					if(projectile.worldObj.rand.nextFloat() < chance && Utils.growPlantAtBlock(projectile.worldObj, pos.add(i, j, k)))
					{
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	private int getRadiusOfGrowth(float potency)
	{
		return (int)(potency * potency);
	}
	
	private float getChanceOfGrowth(float potency)
	{
		return potency * 0.5f;
	}
	
	@Override
	public float getPotency() 
	{
		return this.potency;
	}
}
