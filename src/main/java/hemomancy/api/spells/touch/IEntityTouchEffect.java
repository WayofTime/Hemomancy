package hemomancy.api.spells.touch;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IEntityTouchEffect 
{
	/**
	 * @param player
	 * @param hitEntity
	 * @return
	 */
	public boolean onHitEntity(EntityPlayer player, EntityLivingBase hitEntity);
	
	public float getPotency();
}
