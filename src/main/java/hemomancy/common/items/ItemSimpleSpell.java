package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.api.mana.ISpellCostClient;
import hemomancy.api.mana.ManaHandler;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellTokenRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSimpleSpell extends Item implements ISpellCostClient
{
	public ItemSimpleSpell()
    {
        super();
        setMaxStackSize(1);
        setCreativeTab(Hemomancy.tabHemomancy);
        this.setUnlocalizedName("itemSimpleSpell");
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
        	if(!world.isRemote)
        	{
            	ManaHandler.setManaOfPlayer(player, 100);
        	}
        }else
        {
        	IFocusToken focus = getPreparedFocus(itemStack);
        	
        	if(focus != null)
        	{
        		focus.onItemRightClick(itemStack, world, player);
        	}
        }
        
        return itemStack;
    }

	@Override
	public float getManaCostForClientRender(ItemStack stack) 
	{
		IFocusToken focus = getPreparedFocus(stack);
    	
    	if(focus != null)
    	{
    		return focus.getManaCost(1);
    	}
    	
    	return 0;
	}

	@Override
	public float getBloodCostForClientRender(ItemStack stack) 
	{
		IFocusToken focus = getPreparedFocus(stack);
    	
    	if(focus != null)
    	{
    		return focus.getBloodCost(1);
    	}
    	
    	return 0;
	}
	
	public IFocusToken getPreparedFocus(ItemStack stack)
	{
    	IFocusToken focus = SpellTokenRegistry.getPreparedFocusFromItemStack(stack);
    	
    	return focus;
	}
}