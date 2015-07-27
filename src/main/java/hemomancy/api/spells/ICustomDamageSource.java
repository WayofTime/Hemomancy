package hemomancy.api.spells;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public interface ICustomDamageSource 
{
	public DamageSource getDamageSourceAgainstEntity(EntityPlayer shooter, Entity hitEntity, float damage);
}
