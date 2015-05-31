package hemomancy.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiHUDElements extends GuiContainer
{
	public GuiHUDElements() 
	{
		super(new ContainerHUDElements());
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2)
    {
        //the parameters for drawString are: string, x, y, color
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) 
	{
		HUDRenderHelper.renderHUDElements(Minecraft.getMinecraft());
	}
	
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	
	private boolean isElementTrapped;
	private HUDElement trappedElement;
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button)
	{
		if(button == 0)
		{
			System.out.println("Mouse X: " + mouseX + ", width: " + mc.displayWidth + ", Mouse Y: " + mouseY + ", height: " + mc.displayHeight);
			if(mouseX < 10 && mouseY < 10)
			{
				this.resetElementsToDefaultPosition();
			}
			
			HUDElement element = HUDRenderHelper.getElementAtCursor(mouseX, mouseY);
			System.out.println("X: " + mouseX + ", Y: " + mouseY);

			if(element != null)
			{
				trappedElement = element;
				isElementTrapped = true;
				
				lastMouseX = mouseX;
				lastMouseY = mouseY;
			}
		}else
		{
			trappedElement = null;
			isElementTrapped = false;
		}
	}
	
	/**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
	@Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClicked)
    {
		if(lastButtonClicked == 0 && isElementTrapped && trappedElement != null)
		{
			ScaledResolution scaled = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			trappedElement.xPos = ((float)(mouseX - lastMouseX))/(float)scaled.getScaledWidth() + trappedElement.xPos;
			trappedElement.yPos = ((float)(mouseY - lastMouseY))/(float)scaled.getScaledHeight() + trappedElement.yPos;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
			
			trappedElement.onPositionChange();
			System.out.println("X: " + mouseX + ", Y: " + mouseY);
		}
    }
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button)
	{
		super.mouseReleased(mouseX, mouseY, button);
		
		if(button == 0)
		{
			trappedElement = null;
			isElementTrapped = false;
		}
	}
	
	public void resetElementsToDefaultPosition()
	{
		HUDRenderHelper.resetElementsToDefaultPosition();
	}
}
