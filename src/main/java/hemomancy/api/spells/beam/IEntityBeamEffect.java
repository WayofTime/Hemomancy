package hemomancy.api.spells.beam;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public interface IEntityBeamEffect 
{
	/**
	 * @param player
	 * @param hitEntity
	 * @param beamTime		Time that the beam has been running
	 * @return
	 */
	public boolean onBeamHitEntity(EntityPlayer player, EntityLivingBase hitEntity, int beamTime);
	
	public float getPotency();
}
