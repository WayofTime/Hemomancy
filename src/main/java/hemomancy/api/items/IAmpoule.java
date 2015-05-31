package hemomancy.api.items;

import net.minecraft.item.ItemStack;

public interface IAmpoule 
{
	/**
	 * @param stack The ItemStack
	 * @return Stored blood inside of the 
	 */
	public float getStoredBlood(ItemStack stack);
	
	/**
	 * @param stack The ItemStack
	 * @param amount The amount of blood that the Ampoule is set to - should not be more than the capacity.
	 */
	public void setStoredBlood(ItemStack stack, float amount);
	
	public float getMaxCapacity(ItemStack stack);
	
	/**
	 * 
	 * @param stack The ItemStack
	 * @param amount Amount added
	 * @return remaining blood that could not fit.
	 */
	public float addBloodToAmpoule(ItemStack stack, float amount);
	
	public float drainBloodFromAmpoule(ItemStack stack, float amount);
}
