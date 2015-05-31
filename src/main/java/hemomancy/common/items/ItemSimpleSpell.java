package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.api.mana.ISpellCostClient;
import hemomancy.api.mana.ManaHandler;
import hemomancy.common.entity.projectile.EntitySpellProjectile;
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
//        		ManaHandler.setMaxManaOfPlayer(player, 100);
            	ManaHandler.setManaOfPlayer(player, 100);
        	}
        }else
        {
//        	if(ApiUtils.drainManaAndBlood(player, 20, 10) && !world.isRemote)
//        	{
//        		EntityArrow arrow = new EntityArrow(world, player, 10);
//        		world.spawnEntityInWorld(arrow);
//        	}
        	EntitySpellProjectile projectile = new EntitySpellProjectile(world, player, 1);
        	world.spawnEntityInWorld(projectile);
        }
        
        return itemStack;
    }

	@Override
	public float getManaCostForClientRender(ItemStack stack) 
	{
		return 20;
	}

	@Override
	public float getBloodCostForClientRender(ItemStack stack) 
	{
		return 10;
	}
}