package hemomancy.client;

import hemomancy.client.hud.BloodHUDElement;
import hemomancy.client.hud.HUDRenderHelper;
import hemomancy.client.hud.ManaHUDElement;
import hemomancy.client.hud.TestingHUDElement;
import hemomancy.common.CommonProxy;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class ClientProxy extends CommonProxy
{
	public static float testingHudElementX = 0;
	public static float testingHudElementY = 0;
	
	public static float manaHudElementX = 0;
	public static float manaHudElementY = 0.5f;
	
	public static float bloodHudElementX = 0;
	public static float bloodHudElementY = 0.6f;
	
    @Override
    public void registerPostSideObjects()
    {

    }
    
    @Override
    public void registerRenderers()
    {
    	super.registerRenderers();
    	HUDRenderHelper.hudElementsMap.put("LPBarTest", new TestingHUDElement(testingHudElementX, testingHudElementY, 32, 32));
    	HUDRenderHelper.hudElementsMap.put("ManaBar", new ManaHUDElement(manaHudElementX, manaHudElementY, 64, 64));
    	HUDRenderHelper.hudElementsMap.put("BloodBar", new BloodHUDElement(bloodHudElementX, bloodHudElementY, 64, 64));
    	
    	ItemRenderRegistry.registerItemRenderers();
    	BlockRenderRegistry.registerBlockRenderers();
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void InitRendering()
    {
    	
    }

    @Override
    public void registerEvents()
    {
    	super.registerEvents();
    	Object ob = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(ob);
        MinecraftForge.EVENT_BUS.register(ob);
    }
}
