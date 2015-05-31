package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellTokenRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpellPage extends Item 
{
	
	
	public IFocusToken getPreparedFocusFromItemStack(ItemStack stack)
	{
		return SpellTokenRegistry.getPreparedFocusFromItemStack(stack);
	}
}
