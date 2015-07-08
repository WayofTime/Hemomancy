package hemomancy.common.entity.ai;

import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.util.Utils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.BlockPos;

public class SummonAIMoveToChest extends EntityAIBase
{
    private EntitySummon theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;

    public SummonAIMoveToChest(EntitySummon summon, double moveSpeed)
    {
        this.theEntity = summon;
        this.movementSpeed = moveSpeed;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if ((!theEntity.isWorking && this.theEntity.inventory.isEmpty()) || (theEntity.isWorking && (Utils.isWithinRangeOfBlock(theEntity, theEntity.dumpChestLocation, 3) || this.theEntity.inventory.getFirstEmptyStack() >= 0))) //Will only dump if full.
        {
            return false;
        }
        else
        {
            BlockPos blockpos = this.theEntity.dumpChestLocation;
            if(blockpos == null)
            {
            	return false;
            }
            
            if(!(theEntity.worldObj.getTileEntity(blockpos) instanceof IInventory))
            {
            	return false;
            }
            
            this.movePosX = blockpos.getX();
            this.movePosY = blockpos.getY();
            this.movePosZ = blockpos.getZ();
            
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return !this.theEntity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}