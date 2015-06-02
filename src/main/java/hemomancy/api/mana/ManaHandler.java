package hemomancy.api.mana;

import hemomancy.api.ApiUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class ManaHandler 
{
	public static int getMaxManaForLevel(int level)
	{
		return (int)(Math.pow(1.2, level - 1) * 100);
	}
	
	public static float getManaOfPlayer(EntityPlayer player)
	{
		return ApiUtils.getManaOfPlayer(player);
	}
	
	public static void setManaOfPlayer(EntityPlayer player, float mana)
	{
		ApiUtils.setManaOfPlayer(player, mana);
		
		setManaSynced(player, false);
	}
	
	public static int getMaxManaOfPlayer(EntityPlayer player)
	{
		return ApiUtils.getMaxManaOfPlayer(player);
	}
	
	public static void setMaxManaOfPlayer(EntityPlayer player, int maxMana)
	{
		ApiUtils.setMaxManaOfPlayer(player, maxMana);
	}
	
	public static float drainManaOfPlayer(EntityPlayer player, float amount)
	{
		float mana = getManaOfPlayer(player);
		
		float drain = Math.min(mana, amount);
		
		setManaOfPlayer(player, mana - drain);
		
		return drain;
	}
	
	public static float addManaToPlayer(EntityPlayer player, float amount)
	{
		float mana = getManaOfPlayer(player);
		
		float added = Math.min(getMaxManaOfPlayer(player) - mana, amount);
		
		setManaOfPlayer(player, mana + added);
		
		return added;
	}
	
	public static void updatePlayerMana(EntityPlayerMP player)
	{
		ApiUtils.syncObject.syncPlayerMana(player);
	}
	
	public static boolean isManaSynced(EntityPlayer player)
	{
		return ApiUtils.isManaSynced(player);
	}
	
	public static void setManaSynced(EntityPlayer player, boolean bool)
	{
		ApiUtils.setManaSynced(player, bool);
	}
}
