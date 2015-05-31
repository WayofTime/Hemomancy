package hemomancy.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class HUDElement 
{
	public float xPos; //Percentage from 0.0 to 1.0. 0.0 is the top left of the screen.
	public float yPos;
	
	public final int xSize;
	public final int ySize;
	
	public static int zLevel = 0;
	
	public HUDElement(float x, float y, int xWidth, int yWidth)
	{
		this.xPos = x;
		this.yPos = y;
		this.xSize = xWidth;
		this.ySize = yWidth;
	}
	
	public void renderHUD(Minecraft mc)
	{
		
	}
	
	public boolean shouldRenderHUD(Minecraft mc)
	{
		return true;
	}
	
	public void onPositionChange(){}
	
	public void resetToDefaultPosition(){}
	
	public static void drawTexturedModalRect(int x, int y, int xPixel, int yPixel, double sizeX, double sizeY)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.startDrawingQuads();
        wr.addVertexWithUV((double)(x + 0), (double)(y + sizeY), (double)zLevel, (double)((float)(xPixel + 0) * f), (double)((float)(yPixel + sizeY) * f1));
        wr.addVertexWithUV((double)(x + sizeX), (double)(y + sizeY), (double)zLevel, (double)((float)(xPixel + sizeX) * f), (double)((float)(yPixel + sizeY) * f1));
        wr.addVertexWithUV((double)(x + sizeX), (double)(y + 0), (double)zLevel, (double)((float)(xPixel + sizeX) * f), (double)((float)(yPixel + 0) * f1));
        wr.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)zLevel, (double)((float)(xPixel + 0) * f), (double)((float)(yPixel + 0) * f1));
        tessellator.draw();
    }
}
