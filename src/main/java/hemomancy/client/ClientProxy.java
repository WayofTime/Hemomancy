package hemomancy.client;

import hemomancy.client.hud.BloodHUDElement;
import hemomancy.client.hud.ExpHUDElement;
import hemomancy.client.hud.HUDRenderHelper;
import hemomancy.client.hud.ManaHUDElement;
import hemomancy.client.hud.TestingHUDElement;
import hemomancy.client.render.RenderEntitySpellProjectile;
import hemomancy.client.render.beam.FXBeam;
import hemomancy.client.render.beam.FXFloatingCircle;
import hemomancy.common.CommonProxy;
import hemomancy.common.entity.projectile.EntitySpellProjectile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class ClientProxy extends CommonProxy
{
	public static float testingHudElementX = 0;
	public static float testingHudElementY = 0;
	
	public static float manaHudElementX = 0;
	public static float manaHudElementY = 0.5f;
	
	public static float bloodHudElementX = 0;
	public static float bloodHudElementY = 0.6f;
	
	public static float expHudElementX = 0;
	public static float expHudElementY = 0.7f;
	
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
    	HUDRenderHelper.hudElementsMap.put("ExpBar", new ExpHUDElement(expHudElementX, expHudElementY, 64, 64));
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderEntitySpellProjectile(Minecraft.getMinecraft().getRenderManager()));
    	Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntitySpellProjectile.class, new RenderEntitySpellProjectile(Minecraft.getMinecraft().getRenderManager()));

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
    
    @Override
    public Object beamCont(World worldObj, EntityPlayer p, double tx, double ty, double tz, int type, int color, boolean reverse, float endmod, Object input, int impact)
    {
	     FXBeam beamcon = null;
	     Color c = new Color(color);
	     if ((input instanceof FXBeam)) 
	     {
	    	 beamcon = (FXBeam)input;
	     }
	     if ((beamcon == null) || (beamcon.isDead))
	     {
		     beamcon = new FXBeam(worldObj, p, tx, ty, tz, c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, 8);
		       
		     beamcon.setType(type);
		     beamcon.setEndMod(endmod);
		     beamcon.setReverse(reverse);
		     FMLClientHandler.instance().getClient().effectRenderer.addEffect(beamcon);
	     }
	     else
	     {
		     beamcon.updateBeam(tx, ty, tz);
		     beamcon.setEndMod(endmod);
		     beamcon.impact = impact;
	     }
	     
	     return beamcon;
    }
    
    @Override
    public Object beamContact(World worldObj, EntityPlayer p, double tx, double ty, double tz, EnumFacing sideHit, int type, int color, boolean reverse, float endmod, Object input, int impact)
    {
	     FXFloatingCircle beamcon = null;
	     Color c = new Color(color);
	     if ((input instanceof FXFloatingCircle)) 
	     {
	    	 beamcon = (FXFloatingCircle)input;
	     }
	     if ((beamcon == null) || (beamcon.isDead))
	     {
		     beamcon = new FXFloatingCircle(worldObj, p, tx, ty, tz, sideHit, c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, 8);
		       
		     beamcon.setType(type);
		     FMLClientHandler.instance().getClient().effectRenderer.addEffect(beamcon);
	     }
	     else
	     {
		     beamcon.updateCircle(tx, ty, tz, sideHit);
		     beamcon.impact = impact;
	     }
	     
	     return beamcon;
    }
}
