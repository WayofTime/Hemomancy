package hemomancy.common.entity.ai;

import hemomancy.common.entity.mob.EntitySummon;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;

public class SummonAIMoveToArea extends EntityAIBase
{
    private EntitySummon theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;

    public SummonAIMoveToArea(EntitySummon summon, double moveSpeed)
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
        if (this.theEntity.isWithinWorkArea())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = this.theEntity.getCentralPositionInBlockArea();
            if(blockpos == null)
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