package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IProjectileDamageModifier;
import hemomancy.common.util.DamageCounterExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class FireProjectileEffect implements IProjectileDamageModifier
{
	@Override
	public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage, float potency) 
	{
		if(hitEntity instanceof EntityLivingBase)
		{
			DamageCounterExtendedProperties properties = DamageCounterExtendedProperties.get((EntityLivingBase)hitEntity);
			int iceCounter = properties.getIceCounters();
			if(iceCounter > 0)
			{
				properties.clearIceCounter();
				return (float) (originalDamage * 0.1f * iceCounter * potency);
			}
		}
		return 0;
	}
}
