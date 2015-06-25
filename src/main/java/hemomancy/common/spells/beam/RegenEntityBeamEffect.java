package hemomancy.common.spells.beam;

import hemomancy.api.spells.beam.IEntityBeamEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class RegenEntityBeamEffect implements IEntityBeamEffect
{
	public float potency;
	
	public RegenEntityBeamEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onBeamHitEntity(EntityPlayer player, EntityLivingBase hitEntity, int beamTime) 
	{
		PotionEffect eff = new PotionEffect(Potion.regeneration.id, (int)(100 * potency), Math.max((int)Math.floor(potency - 0.01), 0));
        if(!hitEntity.isPotionActive(Potion.regeneration) && hitEntity.isPotionApplicable(eff))
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