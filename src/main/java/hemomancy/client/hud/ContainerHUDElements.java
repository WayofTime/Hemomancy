package hemomancy.client.hud;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerHUDElements extends Container
{
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}
