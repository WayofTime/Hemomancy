package hemomancy.common.entity.ai;

import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class SummonAIWoodCutter extends EntityAIBase
{
    private EntitySummon theEntity;
    public float potency;
    public double range = 10;
    
    public int delay = 0;
    
	public static String key = "woodCutter";
    
    public List<BlockPos> logList = new ArrayList();

    public SummonAIWoodCutter(EntitySummon summon, float potency)
    {
        this.theEntity = summon;
        this.potency = potency;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        if(this.theEntity.targetKey == key && Utils.isWithinRangeOfBlock(theEntity, this.theEntity.targetPos, range))
        {
    		System.out.println("Hey");

        	return true;
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
    	if(delay > 0)
    	{
    		delay -= 1;
    		return true;
    	}
    	if(!logList.isEmpty())
    	{
    		BlockPos pos = logList.get(0);
    		IBlockState state = this.theEntity.worldObj.getBlockState(pos);
			logList.remove(pos);
			
    		if(isBlockValid(state.getBlock()))
    		{
    			delay += 20 * state.getBlock().getBlockHardness(this.theEntity.worldObj, pos);
    			Utils.digBlock(this.theEntity.worldObj, pos, state, 10, 0, false);
    			return true;
    		}
    	}
    	
        if(!logList.isEmpty() && this.theEntity.targetKey == key && Utils.isWithinRangeOfBlock(theEntity, this.theEntity.targetPos, range))
        {
        	return true;
        }else
        {
        	this.theEntity.targetKey = "nothing";
        	return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
    	BlockPos pos = this.theEntity.targetPos;
    	boolean test = true;
    	
    	List<BlockPos> positionList = new LinkedList();
    	List<BlockPos> checkedList = new LinkedList();
    	
    	List<BlockPos> logList = new LinkedList();
    	List<BlockPos> leafList = new LinkedList();
    	
    	positionList.add(pos);
    	
    	while(test)
    	{
    		test = false;

        	List<BlockPos> addedList = new LinkedList();
    		
    		for(BlockPos newPos : positionList)
    		{
    			for(EnumFacing facing : EnumFacing.VALUES)
    			{
    				BlockPos testPos = newPos.add(facing.getDirectionVec());
    				if(checkedList.contains(testPos))
    				{
    					continue;
    				}else
    				{
    					checkedList.add(testPos);
    				}
    				
    				IBlockState state = this.theEntity.worldObj.getBlockState(testPos);
    				if(isBlockValid(state.getBlock()))
    				{
    					if(state.getBlock() instanceof BlockLog)
    					{
    						logList.add(testPos);
    					}else
    					{
    						leafList.add(testPos);
    					}
    					
    					addedList.add(testPos);
    					test = true;
    				}
    			}
    		}
    		
    		positionList.addAll(addedList); //TODO: Force the list to add leaves first and then logs.
    	}
    	    	
    	List<BlockPos> posList = leafList;
    	posList.addAll(logList);
    	this.logList = posList;
    }
    
    public boolean isBlockValid(Block block)
    {
    	return block instanceof BlockLog || block instanceof BlockLeavesBase;
    }
}