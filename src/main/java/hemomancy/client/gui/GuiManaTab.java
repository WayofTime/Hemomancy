package hemomancy.client.gui;

import hemomancy.api.inventory.InventoryManaTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class GuiManaTab extends GuiContainer
{
    public GuiManaTab(EntityPlayer player, InventoryManaTab inventoryManaTab)
    {
        super(new ContainerManaTab(player, inventoryManaTab));
        xSize = 176;
        ySize = 121;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2)
    {
        //the parameters for drawString are: string, x, y, color
        fontRendererObj.drawString("Sigil of Holding", 52, 4, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        //draw your Gui here, only thing you need to change is the path
        ResourceLocation test = new ResourceLocation("hemomancy", "textures/gui/MagicInventory.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(test);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
