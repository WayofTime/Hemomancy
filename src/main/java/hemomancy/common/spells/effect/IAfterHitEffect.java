package hemomancy.common.spells.effect;

import net.minecraft.entity.Entity;

public interface IAfterHitEffect 
{
	public boolean applyAfterDamageEffect(Entity shooter, Entity hitEntity, float damageDone);
	
	public float getPotency();
}
