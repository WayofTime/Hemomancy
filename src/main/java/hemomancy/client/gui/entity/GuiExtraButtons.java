package hemomancy.client.gui.entity;

import hemomancy.client.gui.entity.controlButtons.SummonControlButton;
import hemomancy.common.summon.SummonHandler;
import hemomancy.common.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiExtraButtons extends GuiContainer
{
	public static final ResourceLocation resourceLocation = new ResourceLocation("hemomancy", "textures/gui/SpellTinkerer.png");
	public final EntityPlayer player;
	public final UUID summonId;
	public final MovingObjectPosition mop;
	
	public List<SummonControlButton> controlButtonList = new ArrayList();
	
	int circleRadius = 40;
	int numberOfButtonsOnCircle = 20;
	int buttonSize = 16;
	
	double range = 10;
	
	public GuiExtraButtons(EntityPlayer player) //TODO: Write the buttons from the summon onto the player's NBT so that they may see which buttons are allowed. Or grab them from the held item's focus?
	{
		super(new ContainerSummonControl(player));
		this.player = player;
		this.summonId = SummonHandler.getActiveSummon(player);
		this.mop = Utils.getMovingObjectPositionFromPlayer(player.worldObj, player, false, range, true, true);

		controlButtonList.add(new SummonControlButton("textures/tokens/BeamToken.png"));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
		SummonControlButton button = this.getButtonAtLocation(mouseX, mouseY);
		
		if(button != null)
		{
			this.renderToolTip(button, mouseX - x, mouseY - y);
		}
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) 
	{
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(resourceLocation);
//        int x = (width - xSize) / 2;
//        int y = (height - ySize) / 2;
        
        ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        
        GL11.glPushMatrix();
        GL11.glScalef(1f/16f, 1f/16f, 1f/16f);
        
        int buttonNumber = Math.min(numberOfButtonsOnCircle, controlButtonList.size());
        
    	for(int i = 0; i < buttonNumber; i++)
        {
        	SummonControlButton button = controlButtonList.get(i);
        	
        	boolean darkened = false;
        	
        	if(darkened)
        	{
                GL11.glColor4f(0.5F, 0.5F, 0.5F, 0.5F);
        	}

        	this.mc.getTextureManager().bindTexture(button.getResourceLocation());
        	
        	this.drawTexturedModalRect((scaled.getScaledWidth()/2 + getXOffsetForButton(i, controlButtonList.size()) - buttonSize / 2)*16, (scaled.getScaledHeight()/2 + getYOffsetForButton(i, controlButtonList.size()) - buttonSize / 2)*16, 0, 0, 256, 256);
        	
        	if(darkened)
        	{
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	}
        }
        
        
        GL11.glPopMatrix(); 
	}
	
	public int getButtonIndexAtLocation(int mouseX, int mouseY)
	{
        ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int buttonNumber = Math.min(numberOfButtonsOnCircle, controlButtonList.size());
        
        int centerX = scaled.getScaledWidth()/2;
        int centerY = scaled.getScaledHeight()/2;
        
		for(int i=0; i < buttonNumber; i++)
		{
			int xOffset = getXOffsetForButton(i, buttonNumber);
			int yOffset = getYOffsetForButton(i, buttonNumber);
			
			if(mouseX >= centerX + xOffset - buttonSize/2 && mouseX < centerX + xOffset + buttonSize/2 && mouseY >= centerY + yOffset - buttonSize/2 && mouseY < centerY + yOffset + buttonSize/2)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public SummonControlButton getButtonAtLocation(int mouseX, int mouseY)
	{
		int buttonNumber = Math.min(numberOfButtonsOnCircle, controlButtonList.size());
		int buttonIndex = getButtonIndexAtLocation(mouseX, mouseY);
		
		if(buttonIndex >= 0 && buttonIndex < buttonNumber)
		{
			return controlButtonList.get(buttonIndex);
		}
		
		return null;
	}
	
	public int getXOffsetForButton(int button, int buttonNumber)
	{
		float angle = (float) (2 * Math.PI * button / buttonNumber);
		
		return (int) (circleRadius * Math.sin(angle));
	}
	
	public int getYOffsetForButton(int button, int buttonNumber)
	{
		float angle = (float) (2 * Math.PI * button / buttonNumber);
		
		return (int) (-circleRadius * Math.cos(angle));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{		
		if(button == 0)
		{
			SummonControlButton ctrButton = getButtonAtLocation(mouseX, mouseY);
			
			if(ctrButton != null && ctrButton.onClientButtonClicked(this.player, summonId, mop))
			{
				return;
			}
		}
		
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	/**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
	@Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClicked)
    {
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeSinceMouseClicked);
    }
	
	protected void renderToolTip(SummonControlButton button, int x, int y)
    {
        List<String> list = new ArrayList();
        list.add(button.getLocalizedName());
        button.getHoverText(list);

        for (int k = 0; k < list.size(); ++k)
        {
            if (k == 0)
            {
                list.set(k, (String)list.get(k));
            }
            else
            {
                list.set(k, EnumChatFormatting.GRAY + (String)list.get(k));
            }
        }

        this.drawHoveringText(list, x, y, (fontRendererObj));
    }
}
