package hemomancy.common.items;

import hemomancy.Hemomancy;
import hemomancy.api.items.IAmpoule;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemAmpoule extends Item implements IAmpoule
{
	public ItemAmpoule()
	{
		super();
		setMaxStackSize(1);
        setCreativeTab(Hemomancy.tabHemomancy);
        this.setUnlocalizedName("itemAmpoule");
	}
	
	@Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
    {
        list.add("Stored Blood: " + this.getStoredBlood(itemStack) + "/" + this.getMaxCapacity(itemStack));
    }
	
//	@Override
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IIconRegister iconRegister)
//    {
//        this.itemIcon = iconRegister.registerIcon("Hemomancy:Ampoule");
//    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		this.addBloodToAmpoule(itemStack, 1000);
        return itemStack;
    }
	
	@Override
	public float getStoredBlood(ItemStack stack) 
	{
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		
		return tag.getFloat("storedBlood");
	}

	@Override
	public void setStoredBlood(ItemStack stack, float amount) 
	{
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		
		tag.setFloat("storedBlood", amount);
	}

	@Override
	public float getMaxCapacity(ItemStack stack) 
	{
		return 100;
	}

	@Override
	public float addBloodToAmpoule(ItemStack stack, float amount) 
	{
		float blood = this.getStoredBlood(stack);
		float added = Math.min(this.getMaxCapacity(stack) - blood, amount);
		
		this.setStoredBlood(stack, blood + added);
		
		return added;
	}

	@Override
	public float drainBloodFromAmpoule(ItemStack stack, float amount) 
	{
		float blood = this.getStoredBlood(stack);
		float drained = Math.min(blood, amount);
		
		this.setStoredBlood(stack, blood - drained);
		
		return drained;
	}
}
