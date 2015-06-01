package hemomancy.common;

import hemomancy.Hemomancy;
import hemomancy.common.blocks.tileEntity.TESpellTinkerer;
import hemomancy.common.entity.projectile.EntitySpellProjectile;
import hemomancy.common.network.PacketHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy
{
    // Client stuff
    public void registerRenderers()
    {
        // Nothing here as the server doesn't render graphics!
    }
    
    public void registerPostSideObjects()
    {
    	
    }

    public void registerEntities()
    {
    }

    public World getClientWorld()
    {
        return null;
    }

    public void registerActions()
    {
    }

    public void registerEvents()
    {
    	EventHandler eventHook = new EventHandler();
        FMLCommonHandler.instance().bus().register(eventHook);
        MinecraftForge.EVENT_BUS.register(eventHook);
    }

    public void registerSoundHandler()
    {
        // Nothing here as this is a server side proxy
    }

    public void registerTileEntities()
    {
    	GameRegistry.registerTileEntity(TESpellTinkerer.class, "tileSpellTinkerer");
    }

    public void registerEntityTrackers()
    {
    	EntityRegistry.registerModEntity(EntitySpellProjectile.class, "entitySpellProjectile", 0, Hemomancy.instance, 128, 1, true);
    }

    public void registerTickHandlers()
    {
    }

    public void InitRendering()
    {

    }
    
    public void initPacketHandlers()
    {
    	PacketHandler.init();
    }
}
