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
	
	@SubscribeEvent
	public void renderLivingEvent(RenderLivingEvent.Pre event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			long worldTime = event.entity.worldObj.getWorldTime();
			double tickRate = 100;
			double cornerX = worldTime / tickRate;
			double cornerY = worldTime / tickRate;
			double scaleFactor = 1.0/32.0;
			
			double size = 20;
			
			Minecraft mc = Minecraft.getMinecraft();
			
			GL11.glPushMatrix();
			
			GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(new ResourceLocation("hemomancy", "textures/gui/BlankBar1.png"));
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer wr = tessellator.getWorldRenderer();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2, size, -size/2, 0.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2, size, -size/2, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2, 0, -size/2, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2, 0, -size/2, 0.0D + cornerX, 0.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2, 0, size/2, 0.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2, 0, size/2, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2, size, size/2, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(-size/2, size, size/2, 0.0D + cornerX, 1.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(-size/2, 0, -size/2, 0.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2, 0, size/2, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(-size/2, size, size/2, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(-size/2, size, -size/2, 0.0D + cornerX, 1.0D + cornerY);
			tessellator.draw();
			
			wr.startDrawingQuads();
			wr.addVertexWithUV(size/2, size, -size/2, 0.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2, size, size/2, 1.0D + cornerX, 1.0D + cornerY);
			wr.addVertexWithUV(size/2, 0, size/2, 1.0D + cornerX, 0.0D + cornerY);
			wr.addVertexWithUV(size/2, 0, -size/2, 0.0D + cornerX, 0.0D + cornerY);
			tessellator.draw();

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			GL11.glPopMatrix();
		}
	}
}
