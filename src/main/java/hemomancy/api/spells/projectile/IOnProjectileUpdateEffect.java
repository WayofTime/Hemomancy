package hemomancy.api.spells.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public interface IOnProjectileUpdateEffect 
{
	public void onProjectileUpdate(Entity projectile, EntityPlayer shooter, float potency);
}
