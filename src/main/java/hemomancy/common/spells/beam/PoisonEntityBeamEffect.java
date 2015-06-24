package hemomancy.common.spells.beam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PoisonEntityBeamEffect implements IEntityBeamEffect
{
	public float potency;
	
	public PoisonEntityBeamEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onBeamHitEntity(EntityPlayer player, EntityLivingBase hitEntity, int beamTime) 
	{
		PotionEffect eff = new PotionEffect(Potion.poison.id, (int)(100 * potency), Math.max((int)Math.floor(potency - 0.01), 0));
        if(!hitEntity.isPotionActive(Potion.poison) && hitEntity.isPotionApplicable(eff))
        {
        	hitEntity.addPotionEffect(eff);
        	return true;
        }

		return false;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
