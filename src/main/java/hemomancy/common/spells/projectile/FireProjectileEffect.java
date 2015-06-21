package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.api.spells.projectile.IProjectileDamageModifier;
import hemomancy.common.util.DamageCounterExtendedProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FireProjectileEffect implements IProjectileDamageModifier, IOnProjectileCollideEffect
{
    public float potency;

    public FireProjectileEffect(float potency)
    {
        this.potency = potency;
    }

    @Override
    public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage)
    {
        if (hitEntity instanceof EntityLivingBase)
        {
            DamageCounterExtendedProperties properties = DamageCounterExtendedProperties.get((EntityLivingBase) hitEntity);
            int iceCounter = properties.getIceCounters();
            if (iceCounter > 0)
            {
                properties.clearIceCounter();
                return (float) (originalDamage * 0.2f * iceCounter * potency);
            }
        }

        return 0;
    }

    @Override
    public float getPotency()
    {
        return this.potency;
    }

	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
        hitEntity.setFire(2 * (int) potency);

		return !hitEntity.isImmuneToFire();
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
}
