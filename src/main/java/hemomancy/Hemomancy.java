package hemomancy;

import hemomancy.api.ApiUtils;
import hemomancy.api.harvest.HarvestRegistry;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.client.GuiHandler;
import hemomancy.common.CommonProxy;
import hemomancy.common.commands.CommandHUD;
import hemomancy.common.harvest.HemomancyHarvestHandler;
import hemomancy.common.spells.BlockBreakToken;
import hemomancy.common.spells.BounceToken;
import hemomancy.common.spells.ChainToken;
import hemomancy.common.spells.ExplosionToken;
import hemomancy.common.spells.FireSmeltToken;
import hemomancy.common.spells.FireToken;
import hemomancy.common.spells.GrowthToken;
import hemomancy.common.spells.HarvestToken;
import hemomancy.common.spells.HealToken;
import hemomancy.common.spells.IceToken;
import hemomancy.common.spells.LiquidToken;
import hemomancy.common.spells.PlantingToken;
import hemomancy.common.spells.PlasmaBombToken;
import hemomancy.common.spells.PushToken;
import hemomancy.common.spells.SpiritForceToken;
import hemomancy.common.spells.StaticElectricityToken;
import hemomancy.common.spells.StickyToken;
import hemomancy.common.spells.UndeadTurnToken;
import hemomancy.common.spells.WaterToken;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.SelfFocusToken;
import hemomancy.common.spells.focus.TouchFocusToken;
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
        proxy.registerEntityTrackers();

        this.registerSpellTokens();
        this.registerHarvestHandlers();
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

    public void registerSpellTokens()
    {
        SpellTokenRegistry.registerSpellToken("projectileToken", new ProjectileFocusToken());
        SpellTokenRegistry.registerSpellToken("selfToken", new SelfFocusToken());
        SpellTokenRegistry.registerSpellToken("touchToken", new TouchFocusToken());
        SpellTokenRegistry.registerSpellToken("beamToken", new BeamFocusToken());

        SpellTokenRegistry.registerSpellToken("bounceToken", new BounceToken());
        SpellTokenRegistry.registerSpellToken("stickyToken", new StickyToken());
        SpellTokenRegistry.registerSpellToken("explosionToken", new ExplosionToken());
        SpellTokenRegistry.registerSpellToken("liquidToken", new LiquidToken());
        SpellTokenRegistry.registerSpellToken("iceToken", new IceToken());
        SpellTokenRegistry.registerSpellToken("fireToken", new FireToken());
        SpellTokenRegistry.registerSpellToken("blockBreakToken", new BlockBreakToken());
        SpellTokenRegistry.registerSpellToken("fireSmeltToken", new FireSmeltToken());
        SpellTokenRegistry.registerSpellToken("plasmaBombToken", new PlasmaBombToken());
        SpellTokenRegistry.registerSpellToken("pushToken", new PushToken());
        SpellTokenRegistry.registerSpellToken("waterToken", new WaterToken());
        SpellTokenRegistry.registerSpellToken("chainToken", new ChainToken());
        SpellTokenRegistry.registerSpellToken("spiritForceToken", new SpiritForceToken());
        SpellTokenRegistry.registerSpellToken("undeadTurnToken", new UndeadTurnToken());
        SpellTokenRegistry.registerSpellToken("growthToken", new GrowthToken());
        SpellTokenRegistry.registerSpellToken("harvestToken", new HarvestToken());
        SpellTokenRegistry.registerSpellToken("plantingToken", new PlantingToken());
        SpellTokenRegistry.registerSpellToken("healToken", new HealToken());
        SpellTokenRegistry.registerSpellToken("staticElectricityToken", new StaticElectricityToken());
    }

    public void registerHarvestHandlers()
    {
        HarvestRegistry.registerHarvestHandler(new HemomancyHarvestHandler());
    }
}
