package hemomancy.client.gui.entity.controlButtons;

import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SummonBlockPosPacketProcessor;
import hemomancy.common.summon.SummonHandler;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class MarkWorkAreaButton extends SummonControlButton
{
	public MarkWorkAreaButton() 
	{
		super("textures/tokens/SelfToken.png");
	}
	
	@Override
	public boolean onClientButtonClicked(EntityPlayer player, UUID id, MovingObjectPosition mop)
	{
		if(id != null && mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			PacketHandler.INSTANCE.sendToServer(new SummonBlockPosPacketProcessor(SummonBlockPosPacketProcessor.DEFINE_WORK_AREA, id, SummonHandler.getKeyStringForPlayer(Minecraft.getMinecraft().thePlayer), mop.getBlockPos()));
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Define Work Area";
	}
	
	@Override
	public void getHoverText(List<String> list)
	{
		list.add("Defines the work area that");
		list.add("this summon will look after.");
	}
}
