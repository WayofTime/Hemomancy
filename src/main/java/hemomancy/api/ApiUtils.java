package hemomancy.api;

import hemomancy.api.mana.BloodHandler;
import hemomancy.api.mana.ManaHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class ApiUtils 
{
	public static String IDTag = "[Hemomancy]";

	public static IPlayerSyncHandler syncObject;
	
	/**
	 * Thanks Kihira! <3
	 * @param player
	 * @return persistent data tag
	 */
	public static NBTTagCompound getPersistentDataTag(EntityPlayer player) 
	{ 
		NBTTagCompound forgeData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG); 
        NBTTagCompound hemomancyData = forgeData.getCompoundTag("Hemomancy"); 

        //Creates/sets the tags if they don't exist 
        if (!forgeData.hasKey("Hemomancy")) forgeData.setTag("Hemomancy", hemomancyData); 
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, forgeData); 

        return hemomancyData; 
	}
	
	public static boolean drainManaAndBlood(EntityPlayer player, float mana, float blood)
	{
		float curMana = ManaHandler.getManaOfPlayer(player);
		float curBlood = BloodHandler.getBloodOfPlayer(player);
		
		if(mana <= curMana && blood <= curBlood)
		{
			ManaHandler.drainManaOfPlayer(player, mana);
			BloodHandler.drainBloodOfPlayer(player, blood);
			
			return true;
		}
		return false;
	}
	
	public static NBTTagCompound getManaInventoryTagOfPlayer(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "ManaInventory"))
		{
			return data.getCompoundTag(IDTag + "ManaInventory");
		}
		
		return new NBTTagCompound();
	}
	
	public static void setManaInventoryTagOfPlayer(EntityPlayer player, NBTTagCompound tag)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setTag(IDTag + "ManaInventory", tag);
	}
	
	public static float getManaOfPlayer(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "Mana"))
		{
			return data.getFloat(IDTag + "Mana");
		}
		
		return 0;
	}
	
	public static void setManaOfPlayer(EntityPlayer player, float mana)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setFloat(IDTag + "Mana", mana);
	}
	
	public static int getMaxManaOfPlayer(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "MaxMana"))
		{
			return data.getInteger(IDTag + "MaxMana");
		}
		
		return 0;
	}
	
	public static void setMaxManaOfPlayer(EntityPlayer player, int mana)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "MaxMana", mana);
	}
	
	public static boolean isManaSynced(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "IsManaSynced"))
		{
			return data.getBoolean(IDTag + "IsManaSynced");
		}
		
		return false;
	}
	
	public static void setManaSynced(EntityPlayer player, boolean bool)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setBoolean(IDTag + "IsManaSynced", bool);
	}
	
	public static float getClientBloodOfPlayer(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "Blood"))
		{
			return data.getFloat(IDTag + "Blood");
		}
		
		return 0;
	}
	
	public static void setClientBloodOfPlayer(EntityPlayer player, float Blood)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setFloat(IDTag + "Blood", Blood);
	}
	
	public static float getMaxClientBloodOfPlayer(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "MaxBlood"))
		{
			return data.getFloat(IDTag + "MaxBlood");
		}
		
		return 0;
	}
	
	public static void setMaxClientBloodOfPlayer(EntityPlayer player, float Blood)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setFloat(IDTag + "MaxBlood", Blood);
	}
	
	public static boolean isClientBloodSynced(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "IsBloodSynced"))
		{
			return data.getBoolean(IDTag + "IsBloodSynced");
		}
		
		return false;
	}
	
	public static void setClientBloodSynced(EntityPlayer player, boolean bool)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setBoolean(IDTag + "IsBloodSynced", bool);
	}
	
	public static int getCurrentExp(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "CurExp"))
		{
			return data.getInteger(IDTag + "CurExp");
		}
		
		return 0;
	}
	
	public static int getExpToNext(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "NextExp"))
		{
			return data.getInteger(IDTag + "NextExp");
		}
		
		return 0;
	}

	public static int getLevel(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "Level"))
		{
			return data.getInteger(IDTag + "Level");
		}
		
		return 0;
	}
}
