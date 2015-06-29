package hemomancy.client;

import hemomancy.api.inventory.InventoryManaTab;
import hemomancy.client.gui.ContainerManaTab;
import hemomancy.client.gui.ContainerSpellTinkerer;
import hemomancy.client.gui.GuiManaTab;
import hemomancy.client.gui.GuiSpellTinkerer;
import hemomancy.client.gui.InventorySpellTinkerer;
import hemomancy.client.gui.entity.ContainerSummonControl;
import hemomancy.client.gui.entity.GuiSummonControl;
import hemomancy.client.hud.ContainerHUDElements;
import hemomancy.client.hud.GuiHUDElements;
import hemomancy.common.blocks.tileEntity.TESpellTinkerer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler
{
	public static final int MANA_GUI = 0;
	public static final int HUD_GUI = 1;
	public static final int SPELL_TINKERER_GUI = 2;
	public static final int SUMMON_GUI = 3;
	
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
    	TileEntity tile;
        switch (id)
        {
        case MANA_GUI:
        	return new ContainerManaTab(player, new InventoryManaTab(player));
        case HUD_GUI:
        	return new ContainerHUDElements();
        case SPELL_TINKERER_GUI:
        	tile = world.getTileEntity(new BlockPos(x, y, z));
        	if(tile instanceof TESpellTinkerer)
        	{
        		return new ContainerSpellTinkerer(player, new InventorySpellTinkerer(player, (TESpellTinkerer)tile));
        	}
        case SUMMON_GUI:
        	return new ContainerSummonControl(player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
    	TileEntity tile;
        switch (id)
        {
        case MANA_GUI:
        	return new GuiManaTab(player, new InventoryManaTab(player));
        case HUD_GUI:
        	return new GuiHUDElements();
        case SPELL_TINKERER_GUI:
        	tile = world.getTileEntity(new BlockPos(x, y, z));
        	if(tile instanceof TESpellTinkerer)
        	{
        		return new GuiSpellTinkerer(player, new InventorySpellTinkerer(player, (TESpellTinkerer)tile));
        	}
        case SUMMON_GUI:
        	return new GuiSummonControl(player);
        }

        return null;
    }
}