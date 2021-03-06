package hemomancy.common;

import hemomancy.api.ApiUtils;
import hemomancy.api.mana.BloodHandler;
import hemomancy.api.mana.ManaHandler;
import hemomancy.common.util.DamageCounterExtendedProperties;
import hemomancy.common.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;


public class EventHandler 
{
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event)
	{
		if(event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.entityLiving;
						
			if(!player.worldObj.isRemote && player instanceof EntityPlayerMP)
			{
				if(player.worldObj.getTotalWorldTime() % 20 == 0)
				{
					ManaHandler.addManaToPlayer(player, ManaHandler.getMaxManaOfPlayer(player) / 120f);
				}
				if(!ManaHandler.isManaSynced(player))
				{
					ManaHandler.updatePlayerMana((EntityPlayerMP)player);
					ManaHandler.setManaSynced(player, true);
				}
				
				if(!ApiUtils.isClientBloodSynced(player))
				{
					ApiUtils.syncObject.syncPlayerBlood((EntityPlayerMP)player);
					ApiUtils.setClientBloodSynced(player, true);
				}
				
				if(!Utils.isExpSynced(player))
				{
					ApiUtils.syncObject.syncPlayerExp((EntityPlayerMP)player);
					Utils.setExpSynced(player, true);
				}
			}
		}
		
		if(event.entityLiving.worldObj.isRemote && DamageCounterExtendedProperties.get(event.entityLiving).getIceCounters() > 0)
		{
			System.out.println("I has ice counter");
		}
	}
	
	//Used to sync the client's mana levels on login
	@SubscribeEvent
	public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if(!event.player.worldObj.isRemote)
		{		
			ManaHandler.setManaSynced(event.player, false);
			ApiUtils.setClientBloodSynced(event.player, false);
			Utils.setExpSynced(event.player, false);
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if(event.entity instanceof EntityLivingBase)
		{
			DamageCounterExtendedProperties.register((EntityLivingBase)event.entity);
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void onEntityDamaged(LivingHurtEvent event)
	{
		if(!event.source.isProjectile() && event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)event.source.getEntity();
			
			float amount = Math.min(event.ammount, event.entityLiving.getHealth());
			
			BloodHandler.fillBloodOfPlayer(player, amount);
		}
	}
}
