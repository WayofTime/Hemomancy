package hemomancy.client.hud;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class HUDRenderHelper 
{
	public static Map<String, HUDElement> hudElementsMap = new HashMap<String, HUDElement>();
	public static boolean enabled = true;
	public static boolean showInChat = true;
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean renderHUDElements(Minecraft mc)
	{
		for(Entry<String, HUDElement> entry : hudElementsMap.entrySet())
		{
			entry.getValue().renderHUD(mc);
		}
		
		return true;
	}
	
	public static HUDElement getElementAtCursor(int mouseX, int mouseY)
	{
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		for(Entry<String, HUDElement> entry : hudElementsMap.entrySet())
		{
			HUDElement element = entry.getValue();
			if(mouseX >= element.xPos * scaled.getScaledWidth() && mouseX < element.xPos * scaled.getScaledWidth() + element.xSize && mouseY >= element.yPos * scaled.getScaledHeight() && mouseY < element.yPos* scaled.getScaledHeight() + element.ySize)
			{
				return element;
			}
		}
		
		return null;
	}
	
	public static void resetElementsToDefaultPosition()
	{
		for(Entry<String, HUDElement> entry : hudElementsMap.entrySet())
		{
			HUDElement element = entry.getValue();
			element.resetToDefaultPosition();
		}
	}
}
