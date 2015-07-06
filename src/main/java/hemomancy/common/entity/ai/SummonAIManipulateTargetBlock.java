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

        return this.theEntity.performActionOnBlockInRange(4);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return this.theEntity.performActionOnBlockInRange(4);
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