package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WaterProjectileEffect implements IOnProjectileCollideEffect
{

    public float potency;

    public WaterProjectileEffect(float potency)
    {
        this.potency = potency;
    }

    @Override
    public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity)
    {
        return this.createWaterAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY + hitEntity.getEyeHeight()/2.0, hitEntity.posZ, potency);
    }

    @Override
    public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit)
    {
        return this.createWaterAtPoint(projectile, shooter, pos.getX() + sideHit.getFrontOffsetX(), pos.getY() + sideHit.getFrontOffsetY(), pos.getZ() + sideHit.getFrontOffsetZ(), potency);
    }

    @Override
    public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit)
    {
        return this.createWaterAtPoint(projectile, shooter, pos.getX() + sideHit.getFrontOffsetX(), pos.getY() + sideHit.getFrontOffsetY(), pos.getZ() + sideHit.getFrontOffsetZ(), potency);
    }

    @Override
    public float getPotency()
    {
        return potency;
    }

    public boolean createWaterAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
    {
        int xPos = (int) Math.floor(x);
        int yPos = (int) Math.floor(y);
        int zPos = (int) Math.floor(z);
        int intPotency = getRadiusForPotency(potency);
        World world = projectile.worldObj;
        boolean placed = false;

        for (int i = xPos - intPotency; i <= xPos + intPotency; i++)
        {
            for (int j = yPos - intPotency; j <= yPos + intPotency; j++)
            {
                for (int k = zPos - intPotency; k <= zPos + intPotency; k++)
                {
                    BlockPos blockPos = new BlockPos(i, j, k);
                    if (world.isAirBlock(blockPos))
                    {
                        if(world.setBlockState(blockPos, Blocks.flowing_water.getDefaultState()))
                        {
                        	placed = true;
                        }
                    }
                }
            }
        }
        
        return placed;
    }
    
    public int getRadiusForPotency(float potency)
    {
    	return Math.max(0, (int)potency - 1);
    }
}
