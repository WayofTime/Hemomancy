package hemomancy.common.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import hemomancy.api.ApiUtils;

public class Utils extends ApiUtils
{
	public static void setLevel(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "Level", amount);
	}
	
	public static void setExpToNext(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "NextExp", amount);
	}
	
	public static void setCurrentExp(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "CurExp", amount);
	}
	
	public static boolean isExpSynced(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "IsExpSynced"))
		{
			return data.getBoolean(IDTag + "IsExpSynced");
		}
		
		return false;
	}
	
	public static void setExpSynced(EntityPlayer player, boolean bool)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setBoolean(IDTag + "IsExpSynced", bool);
	}
}
