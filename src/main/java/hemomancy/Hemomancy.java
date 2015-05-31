package hemomancy;

import hemomancy.api.ApiUtils;
import hemomancy.client.GuiHandler;
import hemomancy.common.CommonProxy;
import hemomancy.common.commands.CommandHUD;
import hemomancy.common.util.PlayerSyncHandler;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;


@Mod(modid = "Hemomancy", name = "Hemomancy", version = "v0.0.1")

public class Hemomancy 
{
	public static CreativeTabs tabHemomancy = new CreativeTabs("tabHemomancy")
    {
        @Override
        public ItemStack getIconItemStack()
        {
            return new ItemStack(Items.diamond, 1, 0);
        }

        @Override
        public Item getTabIconItem()
        {
            return Items.diamond;
        }
    };
    
    @Instance("Hemomancy")
    public static Hemomancy instance;
    
    @SidedProxy(clientSide = "hemomancy.client.ClientProxy", serverSide = "hemomancy.common.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	HemomancyConfiguration.init(new File(event.getModConfigurationDirectory(), "Hemomancy.cfg"));
    	
    	ModItems.init();
    	ModBlocks.init();
    	
    	ModItems.registerItems();
    	ModBlocks.registerBlocks();
    	
    	proxy.registerEvents();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event)
    {	
    	NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    	ApiUtils.syncObject = new PlayerSyncHandler();
    	
    	proxy.registerRenderers();
    	proxy.initPacketHandlers();
    	proxy.registerTileEntities();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
    
    @Mod.EventHandler
    public void initCommands(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandHUD());
    }
}
