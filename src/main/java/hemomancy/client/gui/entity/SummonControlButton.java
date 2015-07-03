package hemomancy.client.gui.entity;

import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SummonSendToPacketProcessor;
import hemomancy.common.summon.SummonHandler;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

public class SummonControlButton 
{
	public ResourceLocation location;
	
	public SummonControlButton(String location)
	{
		this(new ResourceLocation("hemomancy", location));
	}
	
	public SummonControlButton(ResourceLocation location)
	{
		this.location = location;
	}
	
	public ResourceLocation getResourceLocation()
	{
		return location;
	}
	
	/**
	 * This method is meant to allow the button to send the fact that it was pressed to the client. The modder is required to create and send the packet.
	 * @param id	The id of the entity that is being commanded. 
	 * @param mop	The target that the player is looking at.
	 * @return
	 */
	public boolean onClientButtonClicked(UUID id, MovingObjectPosition mop)
	{
		if(id != null && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			PacketHandler.INSTANCE.sendToServer(new SummonSendToPacketProcessor(id, SummonHandler.getKeyStringForPlayer(Minecraft.getMinecraft().thePlayer), mop.getBlockPos()));
			
			return true;
		}
		
		return false;
	}
	
	public boolean onButtonClicked()
	{
		return false;
	}
	
	public String getLocalizedName()
	{
		return "Name";
	}
	
	public void getHoverText(List<String> list)
	{
		list.add("Button!");
	}
}
