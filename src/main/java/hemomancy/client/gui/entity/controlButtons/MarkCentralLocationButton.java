package hemomancy.client.gui.entity.controlButtons;

import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SummonBlockPosPacketProcessor;
import hemomancy.common.summon.SummonHandler;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class MarkCentralLocationButton extends SummonControlButton
{
	public MarkCentralLocationButton() 
	{
		super("textures/tokens/ProjectileToken.png");
	}
	
	@Override
	public boolean onClientButtonClicked(EntityPlayer player, UUID id, MovingObjectPosition mop)
	{
		if(id != null && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			PacketHandler.INSTANCE.sendToServer(new SummonBlockPosPacketProcessor(SummonBlockPosPacketProcessor.MARK_IDLE_LOCATION, id, SummonHandler.getKeyStringForPlayer(Minecraft.getMinecraft().thePlayer), mop.getBlockPos()));
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Mark Central Location";
	}
	
	@Override
	public void getHoverText(List<String> list)
	{
		list.add("Marks the location that");
		list.add("this summon will guard.");
	}
}
