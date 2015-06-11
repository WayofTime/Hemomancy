package hemomancy.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class DamageCounterExtendedProperties implements IExtendedEntityProperties
{	
	public static final String EXT_PROP_NAME = "HemomancyCounter";
	
	public final EntityLivingBase entityLiving;
	
	public int iceCounter = 0; 
	
	public DamageCounterExtendedProperties(EntityLivingBase entityLiving)
	{
		this.entityLiving = entityLiving;
	}
	
	public static final void register(EntityLivingBase entityLiving)
	{
		entityLiving.registerExtendedProperties(DamageCounterExtendedProperties.EXT_PROP_NAME, new DamageCounterExtendedProperties(entityLiving));
	}
	
	public static final DamageCounterExtendedProperties get(EntityLivingBase entityLiving)
	{
		return (DamageCounterExtendedProperties) entityLiving.getExtendedProperties(EXT_PROP_NAME);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound tag) 
	{
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setInteger("iceCounter", iceCounter);
		
		tag.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) 
	{
		NBTTagCompound properties = tag.getCompoundTag(EXT_PROP_NAME);
		
		this.iceCounter = properties.getInteger("iceCounter");
	}

	@Override
	public void init(Entity entity, World world){}
	
	public int getIceCounters()
	{
		return this.iceCounter;
	}
	
	public void setIceCounters(int counter)
	{
		this.iceCounter = counter;
	}
	
	public boolean addToIceCounter(int amount, int max)
	{
		int added = Math.max(0, Math.min(amount, max - this.iceCounter));
		
		this.iceCounter += added;
		
		if(!entityLiving.worldObj.isRemote)
		{
			
		}
		
		return added > 0;
	}
	
	public void clearIceCounter()
	{
		this.iceCounter = 0;
	}
	
	public void sendInfoToClient()
	{
		this.entityLiving.getEntityId();
		
		
	}
}
