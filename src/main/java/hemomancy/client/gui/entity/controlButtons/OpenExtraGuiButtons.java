package hemomancy.client.gui.entity.controlButtons;

import hemomancy.client.gui.entity.GuiExtraButtons;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class OpenExtraGuiButtons extends SummonControlButton
{
	public OpenExtraGuiButtons() 
	{
		super("textures/tokens/SelfToken.png");
	}
	
	@Override
	public boolean onClientButtonClicked(EntityPlayer player, UUID id, MovingObjectPosition mop)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiExtraButtons(player));
		
		return false;
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Open Other Gui";
	}
	
	@Override
	public void getHoverText(List<String> list)
	{

	}
}
