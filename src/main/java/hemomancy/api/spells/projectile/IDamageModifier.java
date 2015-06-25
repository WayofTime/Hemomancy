package hemomancy.api.spells.projectile;

import net.minecraft.entity.Entity;

public interface IDamageModifier 
{
	public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage);
	
	public float getPotency();
}
