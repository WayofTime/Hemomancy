package hemomancy.common.util;

import hemomancy.api.events.LevelUpEvent;
import hemomancy.api.mana.ManaHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class LevelHandler
{
	public static void levelUpPlayer(EntityPlayer player)
	{
		int curLevel = Utils.getLevel(player);
		if(!MinecraftForge.EVENT_BUS.post(new LevelUpEvent(player, curLevel + 1)))
		{
			curLevel++;
			Utils.setLevel(player, curLevel);
			int maxMana = ManaHandler.getMaxManaForLevel(curLevel);
			ManaHandler.setMaxManaOfPlayer(player, maxMana);
			ManaHandler.setManaOfPlayer(player, maxMana);
			Utils.setExpToNext(player, curLevel);
			Utils.setCurrentExp(player, 0);
		}
	}
	
	public static void forcePlayerLevel(EntityPlayer player, int level)
	{
		Utils.setLevel(player, level);
		int maxMana = ManaHandler.getMaxManaForLevel(level);
		ManaHandler.setMaxManaOfPlayer(player, maxMana);
		ManaHandler.setManaOfPlayer(player, maxMana);
	}
	
	public static int getExpToNextForLevel(int level)
	{
		return (int)(Math.pow(1.2, level-1) * 10);
	}
	
	public static void setLevel(EntityPlayer player, int amount)
	{
		Utils.setLevel(player, amount);
		
		Utils.setExpSynced(player, false);
	}
	
	public static void setExpToNext(EntityPlayer player, int amount)
	{
		Utils.setExpToNext(player, amount);
		
		Utils.setExpSynced(player, false);
	}
	
	public static void setCurrentExp(EntityPlayer player, int amount)
	{
		Utils.setCurrentExp(player, amount);
		
		Utils.setExpSynced(player, false);
	}
	
	public static void increaseExpOfPlayer(EntityPlayer player, int exp)
	{
		int curExp = Utils.getCurrentExp(player);
		curExp += exp;
		if(curExp >= Utils.getExpToNext(player))
		{
			levelUpPlayer(player);
		}else
		{
			setCurrentExp(player, curExp);
		}
	}
}
