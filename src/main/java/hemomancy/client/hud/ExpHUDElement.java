package hemomancy.client.hud;

import hemomancy.HemomancyConfiguration;
import hemomancy.common.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class ExpHUDElement extends HUDElement
{
	public ExpHUDElement(float expHudElementX, float expHudElementY, int xWidth, int yWidth) 
	{
		super(expHudElementX, expHudElementY, xWidth, yWidth);
	}

	@Override
	public void renderHUD(Minecraft mc)
	{
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		EntityPlayer player = mc.thePlayer;
		
		float expAmount = Utils.getCurrentExp(player);
		float expToNext = Utils.getExpToNext(player);
				
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
//    	int xSize = 32;
//    	int ySize = 32;
    	
    	int amount = Math.max((int) (256 *  (1 - (double)(expAmount) / expToNext)), 0);
    	
    	int x = (int)(xPos * scaled.getScaledWidth()) * 4;
        int y = (int)(yPos * scaled.getScaledHeight()) * 4;
        
        GL11.glScalef(1f/4f, 1f/4f, 1f/4f);

        
        ResourceLocation test = new ResourceLocation("hemomancy", "textures/gui/BlankBar1.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(test);


        drawTexturedModalRect(x, y, 0, 0, 256, 256);
                
        ResourceLocation test2 = new ResourceLocation("hemomancy", "textures/gui/BlankBar2.png");
        GL11.glColor4f(0, 1, 0, 0.5F);
        mc.getTextureManager().bindTexture(test2);
        
        drawTexturedModalRect(x + amount, y, amount, 0, 256 - amount, 256);
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}
	
	@Override
	public void onPositionChange()
	{
		HemomancyConfiguration.config.get("ClientSettings", "ExpHUDxOffset", this.xPos).setValue(this.xPos);
		HemomancyConfiguration.config.get("ClientSettings", "ExpHUDyOffset", this.yPos).setValue(this.yPos);
		HemomancyConfiguration.config.save();
	}
	
	@Override
	public void resetToDefaultPosition()
	{
		this.xPos = 0;
		this.yPos = 0.7f;
	}
}
