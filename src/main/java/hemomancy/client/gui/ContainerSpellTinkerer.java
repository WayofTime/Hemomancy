package hemomancy.client.gui;

import hemomancy.ModItems;
import hemomancy.api.items.IAmpoule;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.common.network.PacketHandler;
import hemomancy.common.network.TESpellTinkererPacketProcessor;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;

public class ContainerSpellTinkerer extends Container
{	
    protected final int PLAYER_INVENTORY_ROWS = 3;
    protected final int PLAYER_INVENTORY_COLUMNS = 9;

    private final EntityPlayer player;
    public final InventorySpellTinkerer inventory;

    public ContainerSpellTinkerer(EntityPlayer player, InventorySpellTinkerer inventory)
    {
        this.player = player;
        this.inventory = inventory;
//        int currentSlotHeldIn = player.inventory.currentItem;

//        for (int columnIndex = 4; columnIndex < inventoryColumns; ++columnIndex)
        {
            this.addSlotToContainer(new Slot(inventory, 0, 152, 98));
        }

        for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex)
        {
            for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex)
            {
                this.addSlotToContainer(new Slot(player.inventory, columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 140 + rowIndex * 18));
            }
        }

        for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
        {
            this.addSlotToContainer(new Slot(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 198));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer)
    {
        super.onContainerClosed(entityPlayer);

        if (!entityPlayer.worldObj.isRemote)
        {
            saveInventory(entityPlayer);
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (!player.worldObj.isRemote)
        {
            saveInventory(player);
        }
    }

    public void finalizeChangesToItemStack()
    {
    	if(SpellTokenRegistry.hasFocusToken(inventory.spellCueList))
    	{
    		ItemStack stack = inventory.getStackInSlot(0);
    		if(stack != null && stack.getItem() instanceof ItemWritableBook)
    		{
    			IFocusToken focus = SpellTokenRegistry.getPreparedFocusFromList(inventory.spellCueList);
    			if(focus != null)
    			{
    				ItemStack newStack = new ItemStack(ModItems.itemSimpleSpell);
    				newStack = SpellTokenRegistry.writeSpellTokensToItemStack(newStack, inventory.spellCueList);
        			newStack.setStackDisplayName("Spell~");
        			
        			Slot slotObject = (Slot) inventorySlots.get(0);
                	slotObject.putStack(newStack);
                	slotObject.onSlotChanged();
                	if(player.worldObj.isRemote)
                	{
                    	sendItemStackToServer(newStack);
                	}
                	this.saveInventory(player);
                	this.detectAndSendChanges();
                	
                	this.removeAllFromCue();
    			}
    		}
    	}	
    }
    
    public void sendItemStackToServer(ItemStack stack)
    {
    	PacketHandler.INSTANCE.sendToServer(new TESpellTinkererPacketProcessor(stack, inventory.tile.getPos(), inventory.tile.getWorld()));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotIndex)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slotIndex);
        int slots = inventorySlots.size();

        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            if (stack.getItem() instanceof IAmpoule)
            {
                if (slotIndex < 1)
                {
                    if (!this.mergeItemStack(stackInSlot, 1, slots, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(stackInSlot, 0, 1, false))
                {
                    return null;
                }
            }
            else if (stack.getItem() instanceof IAmpoule)
            {
                if (slotIndex < 1 + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS))
                {
                    if (!this.mergeItemStack(stackInSlot, 1 + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), inventorySlots.size(), false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(stackInSlot, 1, 1 + (PLAYER_INVENTORY_ROWS * PLAYER_INVENTORY_COLUMNS), false))
                {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            } else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }

    public void saveInventory(EntityPlayer entityPlayer)
    {
        inventory.onGuiSaved(entityPlayer);
    }
    
    public boolean clickOptionsTokenAtIndex(int index)
    {
    	if(index >= 0 && index < inventory.tokenList.size())
    	{
    		SpellToken movedToken = inventory.tokenList.get(index);
    		
    		if((inventory.spellCueList.isEmpty() ? (movedToken instanceof IFocusToken) : !(movedToken instanceof IFocusToken)) && !inventory.darkenedList.contains(movedToken.key))
    		{
    			for(SpellToken token : inventory.spellCueList)
        		{
        			if(!token.isSpellTokenCompatible(inventory.spellCueList, movedToken) || !movedToken.isSpellTokenCompatible(inventory.spellCueList, token))
        			{
        				return false;
        			}
        		}
    			    			
        		inventory.spellCueList.add(movedToken);
        		inventory.darkenedList.add(movedToken.key);
        		return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean clickCueTokenAtIndex(int index)
    {
    	if(index >= 0 && index < inventory.spellCueList.size())
    	{
    		SpellToken removedToken = inventory.spellCueList.remove(index);
    		inventory.darkenedList.remove(removedToken.key);
    		
    		if(removedToken instanceof IFocusToken)
    		{
        		recheckAllTokens();
    		}
    		return true;
    	}
    	
    	return false;
    }  
    
    public void recheckAllTokens()
    {
    	boolean removeAll = true;
    	
    	for(SpellToken token : inventory.spellCueList)
		{
			if(token instanceof IFocusToken)
			{
				removeAll = false;
				break;
			}
		}
    	
    	if(removeAll)
    	{
    		removeAllFromCue();
    	}
    }
    
    public void removeAllFromCue()
    {    	
    	inventory.spellCueList = new ArrayList();
    	inventory.darkenedList = new ArrayList();
    }
}
