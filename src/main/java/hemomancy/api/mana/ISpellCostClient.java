package hemomancy.api.mana;

import net.minecraft.item.ItemStack;

public interface ISpellCostClient 
{
	/**
	 * Returns the cost of the spell that the client uses to render the mana bar.
	 * @param stack
	 * @return cost in mana
	 */
	public float getManaCostForClientRender(ItemStack stack);
	
	public float getBloodCostForClientRender(ItemStack stack);
}
