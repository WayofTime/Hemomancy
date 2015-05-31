package hemomancy.common.blocks.tileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

public class TESpellTinkerer extends TileEntity implements IInventory
{
	public ItemStack[] inventory;
	
	public TESpellTinkerer()
	{
		super();
		inventory = new ItemStack[1];
	}
	
	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagList inventoryList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(stackTag);
                inventoryList.appendTag(stackTag);
            }
        }

        tag.setTag("Inventory", inventoryList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList inventoryList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < inventoryList.tagCount(); i++)
        {
            NBTTagCompound stackTag = inventoryList.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot");

            if (slot >= 0 && slot < inventory.length)
                inventory[slot] = ItemStack.loadItemStackFromNBT(stackTag);
        }
    }
	
	@Override
	public int getSizeInventory() 
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return inventory[slot];
	}

	@Override
    public ItemStack decrStackSize(int slot, int amt)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.stackSize <= amt)
                setInventorySlotContents(slot, null);
            else
            {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0)
                    setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

	@Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            setInventorySlotContents(slot, null);
        }
        
        return stack;
    }

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		inventory[slot] = stack;
	}

	@Override
	public String getName() 
	{
		return "SpellTinkerer";
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 1;
	}

	@Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
    }

	@Override
	public void openInventory(EntityPlayer player) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		return false;
	}

	@Override
	public IChatComponent getDisplayName() 
	{
		return null;
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{
		
	}

	@Override
	public int getFieldCount() 
	{
		return 0;
	}

	@Override
	public void clear() 
	{
		
	}
}
