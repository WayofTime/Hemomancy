package hemomancy.api.spells.projectile;

import net.minecraft.entity.Entity;

public interface IProjectileDamageModifier 
{
	public float getDamageAgainstEntity(Entity shooter, Entity hitEntity, double originalDamage, float potency);
}
