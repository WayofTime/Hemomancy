package hemomancy.common.entity.ai;

import hemomancy.common.entity.mob.EntitySummon;
import net.minecraft.entity.ai.EntityAIBase;

public class SummonAIManipulateTargetBlock extends EntityAIBase
{
    private EntitySummon theEntity;

    public SummonAIManipulateTargetBlock(EntitySummon summon)
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
        if (!this.theEntity.workArea)
        {
            return false;
        }

        if(this.theEntity.performActionOnTargetBlockInRange(4))
    	{
        	this.theEntity.isWorking = true;
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
    	if(this.theEntity.performActionOnTargetBlockInRange(4))
    	{
    		return true;
    	}
    	else
    	{
    		this.theEntity.isWorking = false;
    		return false;
    	}
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