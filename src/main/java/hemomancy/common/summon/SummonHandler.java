package hemomancy.common.summon;

import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SummonSyncPacketProcessor;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class SummonHandler 
{
	public static Map<String, List<UUID>> summonMap = new HashMap();
	
	public static Map<String, UUID> activeSummonMap = new HashMap();
	
	public static void registerSummonToPlayer(EntityPlayer player, EntitySummon summon)
	{
		String key = getKeyStringForPlayer(player);

		registerSummonToPlayer(key, summon.getPersistentID());
		
		sendRegistrationToClient(player, key, summon.getPersistentID(), true);
	}
	
	public static void registerSummonToPlayer(String key, UUID id)
	{
		if(!summonMap.containsKey(key))
		{
			summonMap.put(key, new ArrayList());
		}
		
		List<UUID> summonList = summonMap.get(key);
		summonList.add(id);
	}
	
	/**
	 * @param player The player
	 * @param key
	 * @param id
	 * @param register	True if the entity is being registered, false if it is instead being unregistered
	 */
	public static void sendRegistrationToClient(EntityPlayer player, String key, UUID id, boolean register)
	{
		if(player instanceof EntityPlayerMP)
		{
			PacketHandler.INSTANCE.sendTo(new SummonSyncPacketProcessor(id, key), (EntityPlayerMP)player);
		}
	}
	
	public static void removeSummonFromPlayer(EntityPlayer player, UUID id)
	{
		String key = getKeyStringForPlayer(player);
		
		if(summonMap.containsKey(key) && summonMap.get(key).contains(id))
		{
			summonMap.get(key).remove(id);
		}
	}
	
	public static boolean hasRegisteredSummon(EntityPlayer player, UUID id)
	{
		String key = getKeyStringForPlayer(player);
		
		if(summonMap.containsKey(key) && summonMap.get(key).contains(id))
		{
			return true;
		}
		
		return false;
	}
	
	public static void setActiveSummon(EntityPlayer player, UUID id)
	{
		String key = getKeyStringForPlayer(player);
		
		activeSummonMap.put(key, id);
	}
	
	public static UUID getActiveSummon(EntityPlayer player)
	{
		String key = getKeyStringForPlayer(player);
		
		return activeSummonMap.get(key);
	}
	
	public static EntitySummon getSummonEntity(World world, UUID id)
	{
		Entity entity = Utils.getEntityForUUID(world, id);
		if(entity instanceof EntitySummon)
		{
			return (EntitySummon)entity;
		}
		
		return null;
	}
	
	public static String getKeyStringForPlayer(EntityPlayer player)
	{
		return "S" + player.getName();
	}
}
