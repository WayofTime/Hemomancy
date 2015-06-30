package hemomancy.client;

import hemomancy.client.hud.HUDRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	private Minecraft mc = FMLClientHandler.instance().getClient();
	ResourceLocation tempResource = new ResourceLocation("hemomancy", "textures/spells/self/TestingSelfWave.png");
	
	boolean allowTestRender = false;
	
	@SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
        {
            return;
        }else if ((mc.inGameHasFocus || mc.currentScreen == null) && !mc.gameSettings.showDebugInfo)
		{
        	if(allowTestRender)
        	{
//        		EntityPlayer d;
//        		float worldTime = event.renderTickTime;
//    			double tickRate = 1000;
//    			double cornerX = worldTime / tickRate;
//    			double cornerY = worldTime / tickRate;
//    			
//        		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
//
//    			GL11.glPushMatrix();
//    			
//    			GL11.glDisable(GL11.GL_DEPTH_TEST);
//    			GL11.glDepthMask(false);
//    			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//    			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    			GL11.glDisable(GL11.GL_ALPHA_TEST);
//    			mc.getTextureManager().bindTexture(tempResource);
//    			Tessellator tessellator = Tessellator.getInstance();
//    			WorldRenderer wr = tessellator.getWorldRenderer();
//    			wr.startDrawingQuads();
//    			wr.addVertexWithUV(0.0D, (double)scaled.getScaledHeight(), -90.0D, 0.0D + cornerX, 1.0D + cornerY);
//    			wr.addVertexWithUV((double)scaled.getScaledWidth(), (double)scaled.getScaledHeight(), -90.0D, 1.0D + cornerX, 1.0D + cornerY);
//    			wr.addVertexWithUV((double)scaled.getScaledWidth(), 0.0D, -90.0D, 1.0D + cornerX, 0.0D + cornerY);
//    			wr.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D + cornerX, 0.0D + cornerY);
//    			tessellator.draw();
//    			GL11.glDepthMask(true);
//    			GL11.glEnable(GL11.GL_DEPTH_TEST);
//    			GL11.glEnable(GL11.GL_ALPHA_TEST);
//    			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//    	        GL11.glPopMatrix();
        	}
        				
			if (!HUDRenderHelper.renderHUDElements(mc))
	        {
	        	return;
	        }
		} 
    }	
	
	public static void drawTexturedModalRect(int x, int y, int xPixel, int yPixel, double sizeX, double sizeY)
    {
		double zLevel = 1;
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
	
	@SubscribeEvent
	public void renderLivingEvent(RenderLivingEvent.Pre event)
	{
		if(allowTestRender && event.entity instanceof EntityPlayer)
		{
			long worldTime = event.entity.worldObj.getWorldTime();
			double tickRate = 30;
			double cornerX = worldTime / tickRate;
			double cornerY = worldTime / tickRate;
			double scaleFactor = 1.0/32.0;
			
			double xOffset = event.x;
			double yOffset = event.y;
			double zOffset = event.z;
			
			double size = 20;
			
			Minecraft mc = Minecraft.getMinecraft();
			
			GL11.glPushMatrix();
			
			GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(tempResource);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer wr = tessellator.getWorldRenderer();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2 + xOffset, size + yOffset, -size/2 + zOffset, 0.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, size + yOffset, -size/2 + zOffset, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, 0 + yOffset, -size/2 + zOffset, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2 + xOffset, 0 + yOffset, -size/2 + zOffset, 0.0D + cornerX, 0.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2 + xOffset, 0 + yOffset, size/2 + zOffset, 0.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, 0 + yOffset, size/2 + zOffset, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, size + yOffset, size/2 + zOffset, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(-size/2 + xOffset, size + yOffset, size/2 + zOffset, 0.0D + cornerX, 1.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2 + xOffset, 0 + yOffset, -size/2 + zOffset, 0.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2 + xOffset, 0 + yOffset, size/2 + zOffset, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2 + xOffset, size + yOffset, size/2 + zOffset, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(-size/2 + xOffset, size + yOffset, -size/2 + zOffset, 0.0D + cornerX, 1.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(size/2 + xOffset, size + yOffset, -size/2 + zOffset, 0.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, size + yOffset, size/2 + zOffset, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, 0 + yOffset, size/2 + zOffset, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2 + xOffset, 0 + yOffset, -size/2 + zOffset, 0.0D + cornerX, 0.0D + cornerY);
			tessellator.draw();

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			GL11.glPopMatrix();
		}
	}
}
