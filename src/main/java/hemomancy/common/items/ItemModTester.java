package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.client.GuiHandler;
import hemomancy.common.util.LevelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemModTester extends Item
{
	public ItemModTester()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(Hemomancy.tabHemomancy);
        this.setUnlocalizedName("itemModTester");
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            player.openGui(Hemomancy.instance, GuiHandler.MANA_GUI, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            LevelHandler.forcePlayerLevel(player, 1);
            return itemStack;
        }else
        {
        	LevelHandler.levelUpPlayer(player);
        }
        
        return itemStack;
    }
}
