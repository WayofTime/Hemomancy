package hemomancy.common.inventory;

import hemomancy.common.entity.mob.EntitySummon;

import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ReportedException;

public class InventorySummon implements IInventory
{
    /** An array of 36 item stacks indicating the main player inventory (including the visible bar). */
	public int invSize = 5;
    public ItemStack[] mainInventory = new ItemStack[invSize];
    /** An array of 4 item stacks containing the currently worn armor pieces. */
    public ItemStack[] armorInventory = new ItemStack[4];
    /** The player whose inventory this is. */
    public EntitySummon summon;
    /**
     * Set true whenever the inventory changes. Nothing sets it false so you will have to write your own code to check
     * it and reset the value.
     */
    public boolean inventoryChanged;

    public InventorySummon(EntitySummon playerIn)
    {
        this.summon = playerIn;
    }

    private int getInventorySlotContainItem(Item itemIn)
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == itemIn)
            {
                return i;
            }
        }

        return -1;
    }
    
    public boolean isEmpty()
    {
    	for(ItemStack stack : mainInventory)
    	{
    		if(stack != null)
    		{
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * stores an itemstack in the users inventory
     */
    private int storeItemStack(ItemStack stack)
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].getItem() == stack.getItem() && this.mainInventory[i].isStackable() && this.mainInventory[i].stackSize < this.mainInventory[i].getMaxStackSize() && this.mainInventory[i].stackSize < this.getInventoryStackLimit() && (!this.mainInventory[i].getHasSubtypes() || this.mainInventory[i].getMetadata() == stack.getMetadata()) && ItemStack.areItemStackTagsEqual(this.mainInventory[i], stack))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the first item stack that is empty.
     */
    public int getFirstEmptyStack()
    {
        for (int i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * This function stores as many items of an ItemStack as possible in a matching slot and returns the quantity of
     * left over items.
     */
    private int storePartialItemStack(ItemStack stack)
    {
        Item item = stack.getItem();
        int i = stack.stackSize;
        int j = this.storeItemStack(stack);

        if (j < 0)
        {
            j = this.getFirstEmptyStack();
        }

        if (j < 0)
        {
            return i;
        }
        else
        {
            if (this.mainInventory[j] == null)
            {
                this.mainInventory[j] = new ItemStack(item, 0, stack.getMetadata());

                if (stack.hasTagCompound())
                {
                    this.mainInventory[j].setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                }
            }

            int k = i;

            if (i > this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize)
            {
                k = this.mainInventory[j].getMaxStackSize() - this.mainInventory[j].stackSize;
            }

            if (k > this.getInventoryStackLimit() - this.mainInventory[j].stackSize)
            {
                k = this.getInventoryStackLimit() - this.mainInventory[j].stackSize;
            }

            if (k == 0)
            {
                return i;
            }
            else
            {
                i -= k;
                this.mainInventory[j].stackSize += k;
                this.mainInventory[j].animationsToGo = 5;
                return i;
            }
        }
    }

    /**
     * removed one item of specified Item from inventory (if it is in a stack, the stack size will reduce with 1)
     */
    public boolean consumeInventoryItem(Item p_146026_1_)
    {
        int i = this.getInventorySlotContainItem(p_146026_1_);

        if (i < 0)
        {
            return false;
        }
        else
        {
            if (--this.mainInventory[i].stackSize <= 0)
            {
                this.mainInventory[i] = null;
            }

            return true;
        }
    }

    /**
     * Checks if a specified Item is inside the inventory
     */
    public boolean hasItem(Item item)
    {
        int i = this.getInventorySlotContainItem(item);
        return i >= 0;
    }

    /**
     * Adds the item stack to the inventory, returns false if it is impossible.
     */
    public boolean addItemStackToInventory(final ItemStack stack)
    {
        if (stack != null && stack.stackSize != 0 && stack.getItem() != null)
        {
            try
            {
                int i;

                if (stack.isItemDamaged())
                {
                    i = this.getFirstEmptyStack();

                    if (i >= 0)
                    {
                        this.mainInventory[i] = ItemStack.copyItemStack(stack);
                        this.mainInventory[i].animationsToGo = 5;
                        stack.stackSize = 0;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    do
                    {
                        i = stack.stackSize;
                        stack.stackSize = this.storePartialItemStack(stack);
                    }
                    while (stack.stackSize > 0 && stack.stackSize < i);

//                    if (stack.stackSize == i && this.summon.capabilities.isCreativeMode)
//                    {
//                        stack.stackSize = 0;
//                        return true;
//                    }
//                    else
                    {
                        return stack.stackSize < i;
                    }
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(stack.getItem())));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(stack.getMetadata()));
                crashreportcategory.addCrashSectionCallable("Item name", new Callable()
                {
                    public String call()
                    {
                        return stack.getDisplayName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= this.mainInventory.length)
        {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (aitemstack[index] != null)
        {
            ItemStack itemstack;

            if (aitemstack[index].stackSize <= count)
            {
                itemstack = aitemstack[index];
                aitemstack[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = aitemstack[index].splitStack(count);

                if (aitemstack[index].stackSize == 0)
                {
                    aitemstack[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int index)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= this.mainInventory.length)
        {
            aitemstack = this.armorInventory;
            index -= this.mainInventory.length;
        }

        if (aitemstack[index] != null)
        {
            ItemStack itemstack = aitemstack[index];
            aitemstack[index] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }

        aitemstack[index] = stack;
    }

    /**
     * Writes the inventory out as a list of compound tags. This is where the slot indices are used (+100 for armor, +80
     * for crafting).
     */
    public NBTTagList writeToNBT(NBTTagList tagList)
    {
        int i;
        NBTTagCompound nbttagcompound;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                this.mainInventory[i].writeToNBT(nbttagcompound);
                tagList.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)(i + 100));
                this.armorInventory[i].writeToNBT(nbttagcompound);
                tagList.appendTag(nbttagcompound);
            }
        }

        return tagList;
    }

    /**
     * Reads from the given tag list and fills the slots in the inventory with the correct items.
     */
    public void readFromNBT(NBTTagList p_70443_1_)
    {
        this.mainInventory = new ItemStack[invSize];
        this.armorInventory = new ItemStack[4];

        for (int i = 0; i < p_70443_1_.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

            if (itemstack != null)
            {
                if (j >= 0 && j < this.mainInventory.length)
                {
                    this.mainInventory[j] = itemstack;
                }

                if (j >= 100 && j < this.armorInventory.length + 100)
                {
                    this.armorInventory[j - 100] = itemstack;
                }
            }
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.mainInventory.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int index)
    {
        ItemStack[] aitemstack = this.mainInventory;

        if (index >= aitemstack.length)
        {
            index -= aitemstack.length;
            aitemstack = this.armorInventory;
        }

        return aitemstack[index];
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName()
    {
        return "container.inventory";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return false;
    }

    public IChatComponent getDisplayName()
    {
        return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Drop all armor and main inventory items.
     */
    public void dropAllItems()
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null)
            {
                this.summon.dropItem(this.mainInventory[i], true, false);
                this.mainInventory[i] = null;
            }
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null)
            {
                this.summon.dropItem(this.armorInventory[i], true, false);
                this.armorInventory[i] = null;
            }
        }
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void markDirty()
    {
        this.inventoryChanged = true;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.summon.isDead ? false : player.getDistanceSqToEntity(this.summon) <= 64.0D;
    }

    /**
     * Returns true if the specified ItemStack exists in the inventory.
     */
    public boolean hasItemStack(ItemStack stack)
    {
        int i;

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            if (this.armorInventory[i] != null && this.armorInventory[i].isItemEqual(stack))
            {
                return true;
            }
        }

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            if (this.mainInventory[i] != null && this.mainInventory[i].isItemEqual(stack))
            {
                return true;
            }
        }

        return false;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    /**
     * Copy the ItemStack contents from another InventorySummon instance
     */
    public void copyInventory(InventorySummon inventory)
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            this.mainInventory[i] = ItemStack.copyItemStack(inventory.mainInventory[i]);
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            this.armorInventory[i] = ItemStack.copyItemStack(inventory.armorInventory[i]);
        }
    }

    public int getField(int id)
    {
        return 0;
    }

    public void setField(int id, int value) {}

    public int getFieldCount()
    {
        return 0;
    }

    public void clear()
    {
        int i;

        for (i = 0; i < this.mainInventory.length; ++i)
        {
            this.mainInventory[i] = null;
        }

        for (i = 0; i < this.armorInventory.length; ++i)
        {
            this.armorInventory[i] = null;
        }
    }
}