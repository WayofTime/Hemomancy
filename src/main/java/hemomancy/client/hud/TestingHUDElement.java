package hemomancy.client.hud;

import hemomancy.HemomancyConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class TestingHUDElement extends HUDElement
{
	public TestingHUDElement(float testingHudElementX, float testingHudElementY, int xWidth, int yWidth) 
	{
		super(testingHudElementX, testingHudElementY, xWidth, yWidth);
	}
	
	@Override
	public void renderHUD(Minecraft mc)
	{
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int maxAmount = 50;
		int lpAmount = 20;
		GL11.glPushMatrix();
    	
    	int amount = Math.max((int) (256 *  ((double)(maxAmount - lpAmount) / maxAmount)), 0);
    	
    	int x = (int)(xPos * scaled.getScaledWidth()) * 8;
        int y = (int)(yPos * scaled.getScaledHeight()) * 8;
                
        ResourceLocation test2 = new ResourceLocation("hemomancy", "textures/gui/container1.png");
        GL11.glColor4f(1, 0, 0, 1.0F);
        mc.getTextureManager().bindTexture(test2);
        
        GL11.glScalef(1f/8f, 1f/8f, 1f/8f);
        
        drawTexturedModalRect(x, y + amount, 0, amount, 256, 256 - amount);
        
        ResourceLocation test = new ResourceLocation("hemomancy", "textures/gui/lpVial.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(test);
        

        drawTexturedModalRect(x, y, 0, 0, 256, 256);
        
        GL11.glPopMatrix();
	}
	
	@Override
	public void onPositionChange()
	{
		HemomancyConfiguration.config.get("ClientSettings", "AlchemyHUDxOffset", this.xPos).setValue(this.xPos);
		HemomancyConfiguration.config.get("ClientSettings", "AlchemyHUDyOffset", this.yPos).setValue(this.yPos);
		HemomancyConfiguration.config.save();
	}
	
	@Override
	public void resetToDefaultPosition()
	{
		this.xPos = 0;
		this.yPos = 0;
	}
}
