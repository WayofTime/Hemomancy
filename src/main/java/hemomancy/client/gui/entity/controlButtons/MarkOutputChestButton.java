package hemomancy.client.gui.entity.controlButtons;

import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SummonBlockPosPacketProcessor;
import hemomancy.common.summon.SummonHandler;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class MarkOutputChestButton extends SummonControlButton
{
	public MarkOutputChestButton() 
	{
		super("textures/tokens/TouchToken.png");
	}
	
	@Override
	public boolean onClientButtonClicked(UUID id, MovingObjectPosition mop)
	{
		if(id != null && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			PacketHandler.INSTANCE.sendToServer(new SummonBlockPosPacketProcessor(SummonBlockPosPacketProcessor.OUTPUT_CHEST, id, SummonHandler.getKeyStringForPlayer(Minecraft.getMinecraft().thePlayer), mop.getBlockPos()));
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Mark Output Chest";
	}
	
	@Override
	public void getHoverText(List<String> list)
	{
		list.add("Marks the location of");
		list.add("the output chest.");
	}
}
