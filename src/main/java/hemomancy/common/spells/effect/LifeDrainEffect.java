package hemomancy.common.spells.effect;

import hemomancy.api.mana.BloodHandler;
import hemomancy.api.spells.effect.IAfterHitEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class LifeDrainEffect implements IAfterHitEffect
{
	public float potency;
	
	public LifeDrainEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean applyAfterDamageEffect(Entity shooter, Entity hitEntity, float damageDone) 
	{
		if(shooter instanceof EntityPlayer && damageDone > 0)
		{
			float fill = BloodHandler.fillBloodOfPlayer((EntityPlayer)shooter, damageDone / 2.0f * potency);
			
			if(fill > 0)
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}
}
