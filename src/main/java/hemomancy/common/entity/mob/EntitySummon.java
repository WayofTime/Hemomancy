package hemomancy.common.entity.mob;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.api.spells.summon.ISummonBlockManipulator;
import hemomancy.common.entity.ai.SummonAIManipulateTargetBlock;
import hemomancy.common.entity.ai.SummonAIMoveToArea;
import hemomancy.common.entity.ai.SummonAIMoveToNextTargetBlock;
import hemomancy.common.spells.focus.SummonFocusToken;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySummon extends EntityZombie
{
	public float potency = 1;
	public List<SpellToken> tokenList = new ArrayList();
    public SummonFocusToken focus = null;
	
    public List<ISummonBlockManipulator> blockManipulatorList = new ArrayList();
    
	public String targetKey = "";
	
	public BlockPos targetPos = null;
	
	public BlockPos startingBlockArea = new BlockPos(0, 0, 0);
	public BlockPos endingBlockArea = new BlockPos(0, 0, 0);
	public boolean startWasLastSet = false;
	public boolean workAreaWasReset = true;
	
	public BlockPos idleLocation = new BlockPos(0, 0, 0);
	
	public boolean workArea = false;
	public boolean isIdle = true;
	
	public EntitySummon(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntitySummon(World world, double xPos, double yPos, double zPos)
	{
		this(world);
		
		this.tasks.taskEntries.clear();
		this.applyEntityAI();
		
		this.setPosition(xPos, yPos, zPos);
		
		startingBlockArea = new BlockPos(xPos - 5 + 10, yPos - 2, zPos - 5);
		endingBlockArea = new BlockPos(xPos + 5 + 10, yPos + 2, zPos + 5);
				
		targetPos = new BlockPos(this);
		
		workArea = true;
		isIdle = true;
		
		idleLocation = this.getCentralPositionInBlockArea();	
	}
	
	@Override
	protected void applyEntityAI()
    {
//        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0D, true));
        this.tasks.addTask(5, new SummonAIMoveToArea(this, 1.0));
        this.tasks.addTask(4, new SummonAIManipulateTargetBlock(this));
        this.tasks.addTask(6, new SummonAIMoveToNextTargetBlock(this, 1.0));

//        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        
        tagCompound.setFloat("potency", potency);
        tagCompound.setTag("tokenList", SpellTokenRegistry.writeSpellTokensToTag(tokenList, new NBTTagCompound()));
        
        tagCompound.setBoolean("workArea", workArea);
        tagCompound.setBoolean("isIdle", isIdle);
        tagCompound.setBoolean("workAreaWasReset", workAreaWasReset);
        
        tagCompound.setString("targetKey", targetKey);
        
        tagCompound.setInteger("targetX", targetPos.getX());
        tagCompound.setInteger("targetY", targetPos.getY());
        tagCompound.setInteger("targetZ", targetPos.getZ());
        
        tagCompound.setInteger("idleX", idleLocation.getX());
        tagCompound.setInteger("idleY", idleLocation.getY());
        tagCompound.setInteger("idleZ", idleLocation.getZ());
        
        tagCompound.setInteger("startX", startingBlockArea.getX());
        tagCompound.setInteger("startY", startingBlockArea.getY());
        tagCompound.setInteger("startZ", startingBlockArea.getZ());
        
        tagCompound.setInteger("endX", endingBlockArea.getX());
        tagCompound.setInteger("endY", endingBlockArea.getY());
        tagCompound.setInteger("endZ", endingBlockArea.getZ());
        
        tagCompound.setBoolean("startWasLastSet", startWasLastSet);
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        
        this.potency = tagCompound.getFloat("potency");
        
        this.tokenList = SpellTokenRegistry.readSpellTokensFromTag(tagCompound.getCompoundTag("tokenList"));
        IFocusToken focusToken = SpellTokenRegistry.getPreparedFocusFromList(tokenList);
        if(focusToken instanceof SummonFocusToken)
        {
        	this.focus = (SummonFocusToken)focusToken;
        	this.focus.prepareSummon(null, worldObj, this, potency);
        }
        
        workArea = tagCompound.getBoolean("workArea");
        isIdle = tagCompound.getBoolean("isIdle");
        workAreaWasReset = tagCompound.getBoolean("workAreaWasReset");
        
        targetKey = tagCompound.getString("targetKey");
        
        targetPos = new BlockPos(tagCompound.getInteger("targetX"), tagCompound.getInteger("targetY"), tagCompound.getInteger("targetZ"));
        idleLocation = new BlockPos(tagCompound.getInteger("idleX"), tagCompound.getInteger("idleY"), tagCompound.getInteger("idleZ"));
        startingBlockArea = new BlockPos(tagCompound.getInteger("startX"), tagCompound.getInteger("startY"), tagCompound.getInteger("startZ"));
        endingBlockArea = new BlockPos(tagCompound.getInteger("endX"), tagCompound.getInteger("endY"), tagCompound.getInteger("endZ"));
        
        startWasLastSet = tagCompound.getBoolean("startWasLastSet");
    }
	
	@Override
	public void onLivingUpdate()
    {
		this.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 100, 0));
		
		super.onLivingUpdate();
    }
	
	public BlockPos getCentralPositionInBlockArea()
	{
		if(workArea)
		{
			BlockPos pos = new BlockPos((startingBlockArea.getX() + endingBlockArea.getX()) / 2, (startingBlockArea.getY() + endingBlockArea.getY()) / 2, (startingBlockArea.getZ() + endingBlockArea.getZ()) / 2);
			
			return pos;
		}
		
		return null;
	}
	
	public boolean isWithinWorkArea(double xPos, double yPos, double zPos)
	{
		int minX;
		int minY;
		int minZ;
		int maxX;
		int maxY;
		int maxZ;
		
		if(startingBlockArea.getX() < endingBlockArea.getX())
		{
			minX = startingBlockArea.getX();
			maxX = endingBlockArea.getX();
		}else
		{
			maxX = startingBlockArea.getX();
			minX = endingBlockArea.getX();
		}
		
		if(startingBlockArea.getY() < endingBlockArea.getY())
		{
			minY = startingBlockArea.getY();
			maxY = endingBlockArea.getY();
		}else
		{
			maxY = startingBlockArea.getY();
			minY = endingBlockArea.getY();
		}
		
		if(startingBlockArea.getZ() < endingBlockArea.getZ())
		{
			minZ = startingBlockArea.getZ();
			maxZ = endingBlockArea.getZ();
		}else
		{
			maxZ = startingBlockArea.getZ();
			minZ = endingBlockArea.getZ();
		}
		
		return xPos >= minX && xPos < maxX + 1 && yPos >= minY && yPos < maxY + 1 && zPos >= minZ && zPos < maxZ + 1;
	}
	
	public boolean isWithinWorkArea()
	{
		return isWithinWorkArea(posX, posY, posZ);
	}
	
	public boolean isWithinWorkArea(BlockPos pos)
	{
		return isWithinWorkArea(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public BlockPos getNextTargetBlock()
	{
		if(workArea)
		{
			int minX;
			int minY;
			int minZ;
			int maxX;
			int maxY;
			int maxZ;
			
			if(startingBlockArea.getX() < endingBlockArea.getX())
			{
				minX = startingBlockArea.getX();
				maxX = endingBlockArea.getX();
			}else
			{
				maxX = startingBlockArea.getX();
				minX = endingBlockArea.getX();
			}
			
			if(startingBlockArea.getY() < endingBlockArea.getY())
			{
				minY = startingBlockArea.getY();
				maxY = endingBlockArea.getY();
			}else
			{
				maxY = startingBlockArea.getY();
				minY = endingBlockArea.getY();
			}
			
			if(startingBlockArea.getZ() < endingBlockArea.getZ())
			{
				minZ = startingBlockArea.getZ();
				maxZ = endingBlockArea.getZ();
			}else
			{
				maxZ = startingBlockArea.getZ();
				minZ = endingBlockArea.getZ();
			}
			
			for(int j = minY; j <= maxY; j++)
			{
				for(int i = minX; i <= maxX; i++)
				{
					for(int k = minZ; k <= maxZ; k++)
					{
						BlockPos pos = new BlockPos(i, j, k);
						
						if(this.canManipulateBlock(pos))
						{
							return pos;
						}
					}
				}
			}
		}

		return null;
	}
	
	public boolean canManipulateBlock(BlockPos pos)
	{
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
//		if((block == Blocks.dirt || block == Blocks.grass) && worldObj.isAirBlock(pos.up()))
//		{
//			return true;
//		}
		
		for(ISummonBlockManipulator effect : this.blockManipulatorList)
		{
			if(effect.canManipulateBlock(this, worldObj, pos, block, state))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean performActionOnBlockInRange(int range)
	{
		BlockPos pos = this.getPosition();
		
		int minX;
		int minY;
		int minZ;
		int maxX;
		int maxY;
		int maxZ;
		
		if(startingBlockArea.getX() < endingBlockArea.getX())
		{
			minX = startingBlockArea.getX();
			maxX = endingBlockArea.getX();
		}else
		{
			maxX = startingBlockArea.getX();
			minX = endingBlockArea.getX();
		}
		
		if(startingBlockArea.getY() < endingBlockArea.getY())
		{
			minY = startingBlockArea.getY();
			maxY = endingBlockArea.getY();
		}else
		{
			maxY = startingBlockArea.getY();
			minY = endingBlockArea.getY();
		}
		
		if(startingBlockArea.getZ() < endingBlockArea.getZ())
		{
			minZ = startingBlockArea.getZ();
			maxZ = endingBlockArea.getZ();
		}else
		{
			maxZ = startingBlockArea.getZ();
			minZ = endingBlockArea.getZ();
		}
		
		for(int j = Math.max(minY, pos.getY() - range); j <= Math.min(maxY, pos.getY() + range); j++)
		{
			for(int i = Math.max(minX, pos.getX() - range); i <= Math.min(maxX, pos.getX() + range); i++)
			{
				for(int k = Math.max(minZ, pos.getZ() - range); k <= Math.min(maxZ, pos.getZ() + range); k++)
				{
					BlockPos nextPos = new BlockPos(i, j, k);

					if(canManipulateBlock(nextPos) && performActionOnBlock(nextPos))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean performActionOnBlock(BlockPos pos)
	{
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
//		if(block == Blocks.dirt || block == Blocks.grass)
//		{
//			worldObj.setBlockState(pos, Blocks.farmland.getStateFromMeta(7));
//			return true;
//		}
		
		for(ISummonBlockManipulator effect : this.blockManipulatorList)
		{
			if(effect.manipulateBlock(this, worldObj, pos, block, state))
			{
				return true;
			}
		}
		
		return false;
	}
}
