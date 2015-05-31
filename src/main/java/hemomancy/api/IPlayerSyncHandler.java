package hemomancy.api;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IPlayerSyncHandler 
{
	public void syncPlayerMana(EntityPlayerMP player);
	
	public void syncPlayerBlood(EntityPlayerMP player);
	
	public void syncPlayerExp(EntityPlayerMP player);
}
