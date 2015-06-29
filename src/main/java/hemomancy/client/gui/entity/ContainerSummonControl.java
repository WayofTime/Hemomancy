package hemomancy.client.gui.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSummonControl extends Container
{
	public final EntityPlayer player;
	
	public ContainerSummonControl(EntityPlayer player)
	{
		super();
		this.player = player;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}