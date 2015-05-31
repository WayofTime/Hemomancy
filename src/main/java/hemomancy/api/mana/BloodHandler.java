package hemomancy.api.mana;

import hemomancy.api.ApiUtils;
import hemomancy.api.inventory.InventoryManaTab;
import hemomancy.api.items.IAmpoule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * This handler is used to gain access to the player's total blood. This handler should also only be used on the Server side. For getting the client-synced blood on the client side,
 * look at ApiUtils{@link}
 *
 */
public class BloodHandler 
{
	public static float getBloodOfPlayer(EntityPlayer player)
	{
		InventoryManaTab inventory = new InventoryManaTab(player);
		ItemStack stack = inventory.getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof IAmpoule)
		{
			return ((IAmpoule)stack.getItem()).getStoredBlood(stack);
		}
		
		return 0;
	}
	
	public static float getMaxBloodOfPlayer(EntityPlayer player)
	{
		InventoryManaTab inventory = new InventoryManaTab(player);
		ItemStack stack = inventory.getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof IAmpoule)
		{
			return ((IAmpoule)stack.getItem()).getMaxCapacity(stack);
		}
		
		return 0;
	}
	
	public static float drainBloodOfPlayer(EntityPlayer player, float amount)
	{
		InventoryManaTab inventory = new InventoryManaTab(player);
		ItemStack stack = inventory.getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof IAmpoule)
		{
			float drain = ((IAmpoule)stack.getItem()).drainBloodFromAmpoule(stack, amount);
			if(drain > 0)
			{
				ApiUtils.setClientBloodSynced(player, false);
			}
			return drain;
		}
		
		return 0;
	}
	
	public static float fillBloodOfPlayer(EntityPlayer player, float amount)
	{
		InventoryManaTab inventory = new InventoryManaTab(player);
		ItemStack stack = inventory.getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof IAmpoule)
		{
			float fill = ((IAmpoule)stack.getItem()).addBloodToAmpoule(stack, amount);
			if(fill > 0)
			{
				ApiUtils.setClientBloodSynced(player, false);
			}
			return fill;
		}
		
		return 0;
	}
}
