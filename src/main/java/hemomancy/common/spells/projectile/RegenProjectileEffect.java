package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class RegenProjectileEffect implements IOnProjectileCollideEffect
{
    public float potency;

    public RegenProjectileEffect(float potency)
    {
        this.potency = potency;
    }

    @Override
    public float getPotency()
    {
        return this.potency;
    }

	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		PotionEffect eff = new PotionEffect(Potion.regeneration.id, (int)(200 * potency), Math.max((int)Math.floor(potency - 0.01), 0));
        if(!hitEntity.isPotionActive(Potion.regeneration) && hitEntity.isPotionApplicable(eff))
        {
        	hitEntity.addPotionEffect(eff);
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
}
