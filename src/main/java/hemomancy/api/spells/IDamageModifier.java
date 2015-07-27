package hemomancy.api.spells;

import net.minecraft.entity.Entity;

public interface IDamageModifier 
{
	public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage);
	
	public float getPotency();
}
