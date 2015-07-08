package hemomancy.common.entity.ai;

import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.util.Utils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class SummonAIDumpToChest extends EntityAIBase
{
    private EntitySummon theEntity;

    public SummonAIDumpToChest(EntitySummon summon)
    {
        this.theEntity = summon;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if (!Utils.isWithinRangeOfBlock(theEntity, theEntity.dumpChestLocation, 4) || !(theEntity.worldObj.getTileEntity(theEntity.dumpChestLocation) instanceof IInventory))
        {
            return false;
        }
        
        System.out.println("Dumping...");

        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return dumpStackIntoChest();
    }
    
    public boolean dumpStackIntoChest()
    {
    	TileEntity tile = theEntity.worldObj.getTileEntity(theEntity.dumpChestLocation);
    	if(!(tile instanceof IInventory))
    	{
    		return false;
    	}
    	
    	IInventory tileInv = (IInventory)tile;
    	
    	for(int slot = 0; slot < theEntity.inventory.getSizeInventory(); slot++)
    	{
    		ItemStack slotStack = theEntity.inventory.getStackInSlot(slot);
    		if(slotStack == null)
    		{
    			continue;
    		}
    		
    		for(int i = 0; i < tileInv.getSizeInventory(); i++)
        	{
        		ItemStack invStack = tileInv.getStackInSlot(i);
        		if(invStack == null)
        		{
        			tileInv.setInventorySlotContents(i, slotStack);
        			theEntity.inventory.setInventorySlotContents(slot, null);
        			
        			return true;
        		}
        	}
    	}
    	
    	
    	return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        return;
    }
}