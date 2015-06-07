package hemomancy;

import hemomancy.client.ClientProxy;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class HemomancyConfiguration 
{
	public static Configuration config;

	public static void init(File configFile)
	{
		config = new Configuration(configFile);

		try
		{
			config.load();
			syncConfig();

		} catch (Exception e)
		{
			//Logger
		} finally
		{
			config.save();
		}
	}

	public static void syncConfig()
	{
		ClientProxy.testingHudElementX = (float) config.get("ClientSettings", "AlchemyHUDxOffset", 0.0f).getDouble();
		ClientProxy.testingHudElementY = (float) config.get("ClientSettings", "AlchemyHUDyOffset", 0.0f).getDouble();
		
		ClientProxy.manaHudElementX = (float) config.get("ClientSettings", "ManaHUDxOffset", 0.0f).getDouble();
		ClientProxy.manaHudElementY = (float) config.get("ClientSettings", "ManaHUDyOffset", 0.5f).getDouble();
		
		ClientProxy.bloodHudElementX = (float) config.get("ClientSettings", "BloodHUDxOffset", 0.0f).getDouble();
		ClientProxy.bloodHudElementY = (float) config.get("ClientSettings", "BloodHUDyOffset", 0.6f).getDouble();
		
		ClientProxy.expHudElementX = (float) config.get("ClientSettings", "ExpHUDxOffset", 0.0f).getDouble();
		ClientProxy.expHudElementY = (float) config.get("ClientSettings", "ExpHUDyOffset", 0.07f).getDouble();
		
		config.save();
	}
}
