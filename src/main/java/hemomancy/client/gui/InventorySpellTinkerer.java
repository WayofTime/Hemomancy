package hemomancy.client.gui;

import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.common.blocks.tileEntity.TESpellTinkerer;
import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.SpellTokenPacketProcessor;
import hemomancy.common.spells.TestingSpellToken;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class InventorySpellTinkerer implements IInventory
{
	public List<SpellToken> tokenList = new ArrayList();
	public List<SpellToken> spellCueList = new ArrayList();
	
    protected ItemStack[] inventory;
    public final TESpellTinkerer tile;

    protected static String NBT_ITEMS = "Items";

    public InventorySpellTinkerer(EntityPlayer player, TESpellTinkerer tile)
    {
        inventory = new ItemStack[1];
        this.tile = tile;

        inventory = tile.inventory;
        
        this.tokenList = getTokenListOfPlayer(player);
    }
    
    public List<SpellToken> getTokenListOfPlayer(EntityPlayer player) //TODO Make this function from server -> client
    {
    	if(!player.worldObj.isRemote && player instanceof EntityPlayerMP)
        {
        	PacketHandler.INSTANCE.sendTo(new SpellTokenPacketProcessor(player), (EntityPlayerMP)player);
        }
    	
    	if(!player.worldObj.isRemote)
    	{
    		SpellTokenRegistry.registerSpellToken("testing", new TestingSpellToken());
    		SpellTokenRegistry.addSpellTokenToPlayer(player, "testing");
    	}
    	List<SpellToken> playerTokenList = SpellTokenRegistry.getTokenListOfPlayer(player);
    	System.out.println("Preparing token list: size is " + playerTokenList.size());
    	
    	return playerTokenList;
    }
    
    public void onGuiSaved(EntityPlayer entityPlayer)
    {
        save(entityPlayer);
    }

    public void save(EntityPlayer player)
    {
        tile.inventory = inventory;
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex)
    {
        return slotIndex >= 0 && slotIndex < this.inventory.length ? inventory[slotIndex] : null;
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount)
    {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null)
        {
            if (itemStack.stackSize <= decrementAmount)
            {
                setInventorySlotContents(slotIndex, null);
            }
            else
            {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0)
                {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex)
    {
        if (inventory[slotIndex] != null)
        {
            ItemStack itemStack = inventory[slotIndex];
            inventory[slotIndex] = null;
            return itemStack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack)
    {
        if (slotIndex >= 0 && slotIndex < this.inventory.length)
        {
            this.inventory[slotIndex] = itemStack;
        }
    }

    @Override
    public String getName()
    {
        return "SigilOfHolding";
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {
        // NOOP
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        // NOOP
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack)
    {
    	return true;
    }

    public static boolean hasTag(ItemStack itemStack, String keyName)
    {
        return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(keyName);
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