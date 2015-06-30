package hemomancy.common.entity.mob;

import hemomancy.common.entity.ai.SummonAIMoveToArea;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntitySummon extends EntityZombie
{
	public BlockPos startingBlockArea = new BlockPos(0,0,0);
	public BlockPos endingBlockArea = new BlockPos(0,0,0);
	
	public boolean workArea = false;
	
	public EntitySummon(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntitySummon(World world, double xPos, double yPos, double zPos)
	{
		this(world);
		
		this.setPosition(xPos, yPos, zPos);
		
		startingBlockArea = new BlockPos(xPos - 5 + 10, yPos - 2, zPos - 5);
		endingBlockArea = new BlockPos(xPos + 5 + 10, yPos + 2, zPos + 5);
		
		workArea = true;
		
		System.out.println("Summon on " + (world.isRemote ? "client" : "server") + " side");
	}
	
	@Override
	protected void applyEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0D, true));
        this.tasks.addTask(3, new SummonAIMoveToArea(this, 1.0));
        
//        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        
        tagCompound.setBoolean("workArea", workArea);
        
        tagCompound.setInteger("startX", startingBlockArea.getX());
        tagCompound.setInteger("startY", startingBlockArea.getY());
        tagCompound.setInteger("startZ", startingBlockArea.getZ());
        
        tagCompound.setInteger("endX", endingBlockArea.getX());
        tagCompound.setInteger("endY", endingBlockArea.getY());
        tagCompound.setInteger("endZ", endingBlockArea.getZ());
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        
        workArea = tagCompound.getBoolean("workArea");
        
        startingBlockArea = new BlockPos(tagCompound.getInteger("startX"), tagCompound.getInteger("startY"), tagCompound.getInteger("startZ"));
        endingBlockArea = new BlockPos(tagCompound.getInteger("endX"), tagCompound.getInteger("endY"), tagCompound.getInteger("endZ"));
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
	
	public boolean isWithinWorkArea()
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
		
		return this.posX > minX && this.posX < maxX + 1 && this.posY > minY && this.posY < maxY + 1 && this.posZ > minZ && this.posZ < maxZ + 1;
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
		if(worldObj.getBlockState(pos).getBlock() == Blocks.dirt && worldObj.isAirBlock(pos.up()))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean performActionOnBlock(BlockPos pos)
	{
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
		if(block == Blocks.dirt)
		{
			worldObj.setBlockState(pos, Blocks.farmland.getStateFromMeta(7));
			return true;
		}
		
		return false;
	}
}
