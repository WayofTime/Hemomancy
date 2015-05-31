package hemomancy.client.gui;

import hemomancy.api.ApiUtils;
import hemomancy.api.inventory.InventoryManaTab;
import hemomancy.api.items.IAmpoule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class ContainerManaTab extends Container
{
    protected final int PLAYER_INVENTORY_ROWS = 3;
    protected final int PLAYER_INVENTORY_COLUMNS = 9;

    private final EntityPlayer player;
    public final InventoryManaTab inventoryManaTab;

    public ContainerManaTab(EntityPlayer player, InventoryManaTab inventoryManaTab)
    {
        this.player = player;
        this.inventoryManaTab = inventoryManaTab;
        int currentSlotHeldIn = player.inventory.currentItem;

//        for (int columnIndex = 4; columnIndex < inventoryColumns; ++columnIndex)
        {
            this.addSlotToContainer(new SlotAmpoule(this, inventoryManaTab, player, 0, 8 + 4 * 36, 15));
        }

        for (int rowIndex = 0; rowIndex < PLAYER_INVENTORY_ROWS; ++rowIndex)
        {
            for (int columnIndex = 0; columnIndex < PLAYER_INVENTORY_COLUMNS; ++columnIndex)
            {
                this.addSlotToContainer(new Slot(player.inventory, columnIndex + rowIndex * 9 + 9, 8 + columnIndex * 18, 39 + rowIndex * 18));
            }
        }

        for (int actionBarIndex = 0; actionBarIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarIndex)
        {
            if (actionBarIndex == currentSlotHeldIn)
            {
                this.addSlotToContainer(new SlotDisabled(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 97));
            }
            else
            {
                this.addSlotToContainer(new Slot(player.inventory, actionBarIndex, 8 + actionBarIndex * 18, 97));
            }
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
            ApiUtils.setClientBloodSynced(entityPlayer, false);
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
        inventoryManaTab.onGuiSaved(entityPlayer);
    }

    private class SlotAmpoule extends Slot
    {
        private final EntityPlayer player;
        private ContainerManaTab ContainerManaTab;

        public SlotAmpoule(ContainerManaTab ContainerManaTab, IInventory inventory, EntityPlayer player, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
            this.player = player;
            this.ContainerManaTab = ContainerManaTab;
        }

        @Override
        public void onSlotChanged()
        {
            super.onSlotChanged();

            if (FMLCommonHandler.instance().getEffectiveSide().isServer())
            {
                ContainerManaTab.saveInventory(player);
            }
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return itemStack.getItem() instanceof IAmpoule;
        }
    }

    private class SlotDisabled extends Slot
    {
        public SlotDisabled(IInventory inventory, int slotIndex, int x, int y)
        {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return false;
        }

        @Override
        public boolean canTakeStack(EntityPlayer player)
        {
            return false;
        }
    }
}
