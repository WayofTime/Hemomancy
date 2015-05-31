package hemomancy.client.hud;

import hemomancy.HemomancyConfiguration;
import hemomancy.api.ApiUtils;
import hemomancy.api.mana.ISpellCostClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BloodHUDElement extends HUDElement
{
	public BloodHUDElement(float manaHudElementX, float manaHudElementY, int xWidth, int yWidth) 
	{
		super(manaHudElementX, manaHudElementY, xWidth, yWidth);
	}

	@Override
	public void renderHUD(Minecraft mc)
	{
		ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
		EntityPlayer player = mc.thePlayer;
		ItemStack heldStack = player.getHeldItem();
		
		float bloodAmount = ApiUtils.getClientBloodOfPlayer(player);
		float maxAmount = ApiUtils.getMaxClientBloodOfPlayer(player);
		
		if(maxAmount <= 0) //There is no ampoule in the player's inventory.
		{
			return;
		}
				
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
//    	int xSize = 32;
//    	int ySize = 32;
    	
    	int amount = Math.max((int) (256 *  (1 - (double)(bloodAmount) / maxAmount)), 0);
    	
    	int x = (int)(xPos * scaled.getScaledWidth()) * 4;
        int y = (int)(yPos * scaled.getScaledHeight()) * 4;
        
        GL11.glScalef(1f/4f, 1f/4f, 1f/4f);

        
        ResourceLocation test = new ResourceLocation("hemomancy", "textures/gui/BlankBar1.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(test);


        drawTexturedModalRect(x, y, 0, 0, 256, 256);
                
        ResourceLocation test2 = new ResourceLocation("hemomancy", "textures/gui/BlankBar2.png");
        GL11.glColor4f(1, 0, 0, 0.5F);
        mc.getTextureManager().bindTexture(test2);
        
        drawTexturedModalRect(x + amount, y, amount, 0, 256 - amount, 256);     
        
        //Glowing part showing what will be syphoned
        
        if(heldStack != null && heldStack.getItem() instanceof ISpellCostClient)
        {
        	float drainAmount = ((ISpellCostClient)heldStack.getItem()).getBloodCostForClientRender(heldStack);
        	if(drainAmount <= bloodAmount)
        	{
        		int highlightAmount = Math.max((int) (256 *  (1 - (double)(drainAmount) / maxAmount)), 0);

            	GL11.glColor4d(66.0/255, 1, 1, 0.11F + 0.19F * (float)Math.pow(Math.sin(mc.theWorld.getWorldTime() / 30f), 2));
                
                drawTexturedModalRect(x + amount, y, highlightAmount, 0, 256 - highlightAmount, 256); 
        	}else
        	{
            	GL11.glColor4d(1, 0.2, 0.2, 0.11F + 0.19F * (float)Math.pow(Math.sin(mc.theWorld.getWorldTime() / 30f), 2));

                drawTexturedModalRect(x, y, 0, 0, 256, 256);
        	}
        }
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
	}
	
	@Override
	public void onPositionChange()
	{
		HemomancyConfiguration.config.get("ClientSettings", "BloodHUDxOffset", this.xPos).setValue(this.xPos);
		HemomancyConfiguration.config.get("ClientSettings", "BloodHUDyOffset", this.yPos).setValue(this.yPos);
		HemomancyConfiguration.config.save();
	}
	
	@Override
	public void resetToDefaultPosition()
	{
		this.xPos = 0;
		this.yPos = 0.6f;
	}
}
