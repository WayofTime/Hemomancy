package hemomancy.common.spells.general;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import hemomancy.api.spells.ICustomDamageSource;

public class PhysicalCustomDamageSource implements ICustomDamageSource
{
	@Override
	public DamageSource getDamageSourceAgainstEntity(EntityPlayer shooter, Entity hitEntity, float damage) 
	{
		return DamageSource.causePlayerDamage(shooter);
	}
}
