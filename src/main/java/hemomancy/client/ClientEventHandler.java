package hemomancy.client;

import hemomancy.client.hud.HUDRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	private Minecraft mcClient = FMLClientHandler.instance().getClient();
	
	@SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
        {
            return;
        }
        
		if ((mcClient.inGameHasFocus || mcClient.currentScreen == null) && !mcClient.gameSettings.showDebugInfo)
		{
			if (!HUDRenderHelper.renderHUDElements(mcClient))
	        {
	        	return;
	        }
		} 
    }
}
