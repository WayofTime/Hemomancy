package hemomancy.client.gui;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class GuiSpellTinkerer extends GuiContainer
{	
	public static final ResourceLocation resourceLocation = new ResourceLocation("hemomancy", "textures/gui/SpellTinkerer.png");
	
	public final int tokenWindowStartX = 7;
	public final int tokenWindowStartY = 7;
	
	public final int tokenRows = 3;
	public final int tokenCollumns = 8;
	
	public final int tokenWindowSizeX = tokenCollumns * 18;
	public final int tokenWindowSizeY = tokenRows * 18;
	
	public final int cueWindowStartX = 7;
	public final int cueWindowStartY = tokenWindowStartY + 18 * 3;
	
	public final int cueRows = 1;
	public final int cueCollumns = 7;
	
	public final int cueWindowSizeX = cueCollumns * 18;
	public final int cueWindowSizeY = cueRows * 18;
	
	public int startingIndex = 0;
	
	public int startingCueIndex = 0;

	public GuiSpellTinkerer(EntityPlayer player, InventorySpellTinkerer inventory)
	{
		super(new ContainerSpellTinkerer(player, inventory));
        xSize = 176;
        ySize = 222; 
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		this.buttonList.add(new TinkererButton(21, this.guiLeft + 151, this.guiTop + 7, resourceLocation, 179, 0));
		this.buttonList.add(new TinkererButton(20, this.guiLeft + 151, this.guiTop + 7 + 18, resourceLocation, 179, 18));
		
		this.buttonList.add(new TinkererButton(31, this.guiLeft + 151 - 18, this.guiTop + 7 + 18*3, resourceLocation, 179 + 18, 0));
		this.buttonList.add(new TinkererButton(30, this.guiLeft + 151, this.guiTop + 7 + 18*3, resourceLocation, 179 + 18, 18));
		
		this.buttonList.add(new TinkererButton(50, this.guiLeft + 151 - 36, this.guiTop + 97, resourceLocation, 179, 36, 36, 18, "DONE"));
	}

	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
		int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
		SpellToken token = this.getSpellTokenAtMouse(mouseX, mouseY);
		if(token == null)
		{
			token = this.getCuecueAtMouse(mouseX, mouseY);
		}
		
		if(token != null)
		{
			this.renderToolTip(token, mouseX - x, mouseY - y);
		}
        //the parameters for drawString are: string, x, y, color
//        fontRendererObj.drawString("Sigil of Holding", 52, 4, 4210752);
    }
	
	protected void renderToolTip(SpellToken token, int x, int y)
    {
        List list = new ArrayList();
        list.add(token.getLocalizedName());

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

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        //draw your Gui here, only thing you need to change is the path    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(resourceLocation);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        GL11.glPushMatrix();
        GL11.glScalef(1f/16f, 1f/16f, 1f/16f);
        
        if(startingIndex >= 0)
        {
        	for(int i=startingIndex*tokenCollumns; i<((ContainerSpellTinkerer)this.inventorySlots).inventory.tokenList.size(); i++)
            {
            	SpellToken token = ((ContainerSpellTinkerer)this.inventorySlots).inventory.tokenList.get(i);
            	int xIndex = (i - startingIndex*tokenCollumns)%tokenCollumns;
            	int yIndex = (i - startingIndex*tokenCollumns)/tokenCollumns;
            	if(yIndex >= tokenRows)
            	{
            		break;
            	}
            	
            	boolean darkened = ((ContainerSpellTinkerer)this.inventorySlots).inventory.darkenedList.contains(token.key) || !canFitInCueList(token);
            	
            	if(darkened)
            	{
                    GL11.glColor4f(0.5F, 0.5F, 0.5F, 0.5F);
            	}

            	this.mc.getTextureManager().bindTexture(token.getResourceLocation());
            	
            	this.drawTexturedModalRect((x + tokenWindowStartX + 1 + xIndex * 18)*16, (y + tokenWindowStartY + 1 + yIndex * 18)*16, 0, 0, 256, 256);
            	
            	if(darkened)
            	{
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            	}
            }
        }
        
        if(startingCueIndex >= 0)
        {
	        for(int i=startingCueIndex*cueCollumns; i<((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList.size(); i++)
	        {
	        	SpellToken cue = ((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList.get(i);
	        	int xIndex = (i - startingCueIndex*cueCollumns)%cueCollumns;
	        	int yIndex = (i - startingCueIndex*cueCollumns)/cueCollumns;
	        	if(yIndex >= cueRows)
	        	{
	        		break;
	        	}
	        	
	        	this.mc.getTextureManager().bindTexture(cue.getResourceLocation());
	        	
	        	this.drawTexturedModalRect((x + cueWindowStartX + 1 + xIndex * 18)*16, (y + cueWindowStartY + 1 + yIndex * 18)*16, 0, 0, 256, 256);
	        }
        }
        
        GL11.glPopMatrix(); 
    }
    
    public boolean canFitInCueList(SpellToken token)
    {
    	boolean canFit = ((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList.isEmpty() ? token instanceof IFocusToken : !(token instanceof IFocusToken);
    	for(SpellToken cueToken : ((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList)
    	{
    		canFit = canFit && token.isSpellTokenCompatible(((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList, cueToken) && cueToken.isSpellTokenCompatible(((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList, token);
    		if(!canFit)
    		{
    			return canFit;
    		}
    	}
    	
    	return canFit;
    }
    
    public boolean clickTokenAtMousePosition(int mouseX, int mouseY)
    {
    	int guiX = -(width - xSize) / 2 + mouseX;
        int guiY = -(height - ySize) / 2 + mouseY;
        
        System.out.println("Gui position: " + guiX + ", " + guiY);
        
        if(guiX >= tokenWindowStartX + 1 && guiX < tokenWindowStartX + tokenWindowSizeX && guiY >= tokenWindowStartY + 1 && guiY < tokenWindowStartY + tokenWindowSizeY)
        {
        	int adjustedX = guiX - tokenWindowStartX - 1; //Adjustment to make sure 0,0 is at the top-left of the first token
        	int adjustedY = guiY - tokenWindowStartY - 1;
        	
        	if(adjustedX % 18 < 16 && adjustedY % 18 < 16)
        	{
        		int xIndex = adjustedX / 18;
        		int yIndex = adjustedY / 18;
        		
        		int index = xIndex + yIndex * tokenCollumns + startingIndex * tokenCollumns;

        		System.out.println("Index: " + xIndex + ", " + yIndex + " -> " + index);
        		
        		return ((ContainerSpellTinkerer)this.inventorySlots).clickOptionsTokenAtIndex(index);
        	}
        }
        
    	return false;
    }
    
    public boolean clickCueTokenAtMousePosition(int mouseX, int mouseY)
    {
    	int guiX = -(width - xSize) / 2 + mouseX;
        int guiY = -(height - ySize) / 2 + mouseY;
        
        System.out.println("Gui position: " + guiX + ", " + guiY);
        
        if(guiX >= cueWindowStartX + 1 && guiX < cueWindowStartX + cueWindowSizeX && guiY >= cueWindowStartY + 1 && guiY < cueWindowStartY + cueWindowSizeY)
        {
        	int adjustedX = guiX - cueWindowStartX - 1; //Adjustment to make sure 0,0 is at the top-left of the first cue
        	int adjustedY = guiY - cueWindowStartY - 1;
        	
        	if(adjustedX % 18 < 16 && adjustedY % 18 < 16)
        	{
        		int xIndex = adjustedX / 18;
        		int yIndex = adjustedY / 18;
        		
        		int index = xIndex + yIndex * cueCollumns + startingCueIndex * cueCollumns; //TODO: Make this scroll sideways instead

        		System.out.println("Index: " + xIndex + ", " + yIndex + " -> " + index);
        		
        		return ((ContainerSpellTinkerer)this.inventorySlots).clickCueTokenAtIndex(index);
        	}
        }
        
    	return false;
    }
    
    protected SpellToken getSpellTokenAtMouse(int mouseX, int mouseY)
    {
    	int guiX = -(width - xSize) / 2 + mouseX;
        int guiY = -(height - ySize) / 2 + mouseY;
                
        if(guiX >= tokenWindowStartX + 1 && guiX < tokenWindowStartX + tokenWindowSizeX && guiY >= tokenWindowStartY + 1 && guiY < tokenWindowStartY + tokenWindowSizeY)
        {
        	int adjustedX = guiX - tokenWindowStartX - 1; //Adjustment to make sure 0,0 is at the top-left of the first token
        	int adjustedY = guiY - tokenWindowStartY - 1;
        	
        	if(adjustedX % 18 < 16 && adjustedY % 18 < 16)
        	{
        		int xIndex = adjustedX / 18;
        		int yIndex = adjustedY / 18;
        		
        		int index = xIndex + yIndex * tokenCollumns + startingIndex * tokenCollumns;
        		
        		return ((ContainerSpellTinkerer)this.inventorySlots).inventory.getSpellTokenAtIndex(index);
        	}
        }
        
        return null;
    }
    
    protected SpellToken getCuecueAtMouse(int mouseX, int mouseY)
    {
    	int guiX = -(width - xSize) / 2 + mouseX;
        int guiY = -(height - ySize) / 2 + mouseY;
                
        if(guiX >= cueWindowStartX + 1 && guiX < cueWindowStartX + cueWindowSizeX && guiY >= cueWindowStartY + 1 && guiY < cueWindowStartY + cueWindowSizeY)
        {
        	int adjustedX = guiX - cueWindowStartX - 1; //Adjustment to make sure 0,0 is at the top-left of the first cue
        	int adjustedY = guiY - cueWindowStartY - 1;
        	
        	if(adjustedX % 18 < 16 && adjustedY % 18 < 16)
        	{
        		int xIndex = adjustedX / 18;
        		int yIndex = adjustedY / 18;
        		
        		int index = xIndex + yIndex * cueCollumns + startingIndex * cueCollumns;
        		
        		return ((ContainerSpellTinkerer)this.inventorySlots).inventory.getCueTokenAtIndex(index);
        	}
        }
        
        return null;
    }
    
    @Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		if(button == 0)
		{
			if(clickTokenAtMousePosition(mouseX, mouseY))
			{
				return;
			}else if(clickCueTokenAtMousePosition(mouseX, mouseY))
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
	
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		switch(clickedButton.id)
		{
		case 20:
			startingIndex = Math.min(startingIndex + 1, Math.max((int)Math.ceil(((ContainerSpellTinkerer)this.inventorySlots).inventory.tokenList.size() / 8.0) - startingIndex - tokenRows, 0));
			break;
		case 21:
			startingIndex = Math.max(0, startingIndex - 1);
			break;
			
		case 30:
			startingCueIndex = Math.min(startingCueIndex + 1, Math.max((int)Math.ceil(((ContainerSpellTinkerer)this.inventorySlots).inventory.spellCueList.size() / 7.0) - startingCueIndex - cueRows, 0));
			break;
		case 31:
			startingCueIndex = Math.max(0, startingCueIndex - 1);
			break;
			
		case 50:
			System.out.println("Side: " + FMLCommonHandler.instance().getSide());
			((ContainerSpellTinkerer)this.inventorySlots).finalizeChangesToItemStack();
			break;
		}
	}
	
	public class TinkererButton extends GuiButton
	{
		private final ResourceLocation resourceLocation;
        private final int xOffset;
        private final int yOffset;

        public TinkererButton(int id, int x, int y, ResourceLocation location, int xOffset, int yOffset)
        {
            this(id, x, y, location, xOffset, yOffset, 18, 18, "");
        }
        
        public TinkererButton(int id, int x, int y, ResourceLocation location, int xOffset, int yOffset, int sizeX, int sizeY, String text)
        {
            super(id, x, y, sizeX, sizeY, text);
            this.resourceLocation = location;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
        
        @Override
        public void drawButton(Minecraft mc, int x, int y)
        {
            if (this.visible)
            {
                mc.getTextureManager().bindTexture(resourceLocation);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;

                this.drawTexturedModalRect(this.xPosition, this.yPosition, xOffset, yOffset, this.width, this.height);

//                if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o))
//                {
//                    mc.getTextureManager().bindTexture(this.field_146145_o);
//                }

//                this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.xOffset, this.yOffset, 18, 18);
            }
        }
	}
}
