package hemomancy.common.entity.mob;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.SpellTokenRegistry;
import hemomancy.api.spells.summon.ISummonBlockManipulator;
import hemomancy.common.entity.ai.SummonAIDumpToChest;
import hemomancy.common.entity.ai.SummonAIManipulateTargetBlock;
import hemomancy.common.entity.ai.SummonAIMoveToArea;
import hemomancy.common.entity.ai.SummonAIMoveToChest;
import hemomancy.common.entity.ai.SummonAIMoveToNextTargetBlock;
import hemomancy.common.inventory.InventorySummon;
import hemomancy.common.spells.focus.SummonFocusToken;
import hemomancy.common.summon.SummonHandler;
import hemomancy.common.util.Utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
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
	
	public BlockPos dumpChestLocation = new BlockPos(0, 0, 0);
	
	public boolean workArea = false;
	public boolean isWorking = false;
	
	public String ownerKey = "";
	
	int invSize = 5;
	
	public InventorySummon inventory;
	
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
		isWorking = true;
		
		idleLocation = this.getCentralPositionInBlockArea();	
		
		inventory = new InventorySummon(this);
	}
	
	public void setOwner(EntityPlayer player)
	{
		this.ownerKey = SummonHandler.getKeyStringForPlayer(player);
	}
	
	@Override
	protected void applyEntityAI()
    {
//        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityMob.class, 1.0D, true));
        
        this.tasks.addTask(9, new SummonAIMoveToArea(this, 1.0));
        this.tasks.addTask(4, new SummonAIManipulateTargetBlock(this));
        this.tasks.addTask(6, new SummonAIMoveToNextTargetBlock(this, 1.0));
        
        this.tasks.addTask(3, new SummonAIMoveToChest(this, 1.0));
        this.tasks.addTask(4, new SummonAIDumpToChest(this));

//        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMob.class, true));
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        
        tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        
        tagCompound.setFloat("potency", potency);
        tagCompound.setTag("tokenList", SpellTokenRegistry.writeSpellTokensToTag(tokenList, new NBTTagCompound()));
        
        tagCompound.setBoolean("workArea", workArea);
        tagCompound.setBoolean("isWorking", isWorking);
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
        
        tagCompound.setInteger("dumpChestX", dumpChestLocation.getX());
        tagCompound.setInteger("dumpChestY", dumpChestLocation.getY());
        tagCompound.setInteger("dumpChestZ", dumpChestLocation.getZ());
        
        tagCompound.setBoolean("startWasLastSet", startWasLastSet);
        
        tagCompound.setString("ownerKey", ownerKey);
    }
	
	@Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        this.inventory = new InventorySummon(this);
        
        NBTTagList nbttaglist = tagCompound.getTagList("Inventory", 10);
        this.inventory.readFromNBT(nbttaglist);
        
        this.potency = tagCompound.getFloat("potency");
        
        this.tokenList = SpellTokenRegistry.readSpellTokensFromTag(tagCompound.getCompoundTag("tokenList"));
        IFocusToken focusToken = SpellTokenRegistry.getPreparedFocusFromList(tokenList);
        if(focusToken instanceof SummonFocusToken)
        {
        	this.focus = (SummonFocusToken)focusToken;
        	this.focus.prepareSummon(null, worldObj, this, potency);
        }
        
        workArea = tagCompound.getBoolean("workArea");
        isWorking = tagCompound.getBoolean("isWorking");
        workAreaWasReset = tagCompound.getBoolean("workAreaWasReset");
        
        targetKey = tagCompound.getString("targetKey");
        
        targetPos = new BlockPos(tagCompound.getInteger("targetX"), tagCompound.getInteger("targetY"), tagCompound.getInteger("targetZ"));
        idleLocation = new BlockPos(tagCompound.getInteger("idleX"), tagCompound.getInteger("idleY"), tagCompound.getInteger("idleZ"));
        startingBlockArea = new BlockPos(tagCompound.getInteger("startX"), tagCompound.getInteger("startY"), tagCompound.getInteger("startZ"));
        endingBlockArea = new BlockPos(tagCompound.getInteger("endX"), tagCompound.getInteger("endY"), tagCompound.getInteger("endZ"));
        
        dumpChestLocation = new BlockPos(tagCompound.getInteger("dumpChestX"), tagCompound.getInteger("dumpChestY"), tagCompound.getInteger("dumpChestZ"));
        
        startWasLastSet = tagCompound.getBoolean("startWasLastSet");
        
        ownerKey = tagCompound.getString("ownerKey");
        
        SummonHandler.registerSummonToPlayer(ownerKey, getPersistentID());
    }
	
	@Override
	public void onLivingUpdate()
    {
		this.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 100, 0));
		
		if(!this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20 == 0)
		{
//			for(int i = 0; i < this.inventory.getSizeInventory(); i++)
//			{
//				System.out.println("In slot " + i + ": " + this.inventory.getStackInSlot(i));
//			}
//			System.out.println((this.inventory == null || this.inventory.getStackInSlot(4) == null) ? "" : this.inventory.getStackInSlot(4));
		}
		
		List<EntityItem> itemList = this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().expand(1, 1, 1));
		
		for(EntityItem item : itemList)
		{
			this.pickUpItem(item);
		}
		
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
						
			for(int j = maxY; j >= minY; j--)
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
	
	public boolean performActionOnTargetBlockInRange(int range)
	{
		if(Utils.isWithinRangeOfBlock(this, targetPos, range))
		{
			if(canManipulateBlock(targetPos) && performActionOnBlock(targetPos))
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
		
		for(int j = Math.min(maxY, pos.getY() + range); j >= Math.max(minY, pos.getY() - range); j--)
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
	
	public boolean canManipulateBlock(BlockPos pos)
	{
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
//		if((block == Blocks.dirt || block == Blocks.grass) && worldObj.isAirBlock(pos.up()))
//		{
//			return true;
//		}
//		System.out.println("Size: " + this.blockManipulatorList.size());
		for(ISummonBlockManipulator effect : this.blockManipulatorList)
		{
			if(effect.canManipulateBlock(this, worldObj, pos, block, state))
			{
				return true;
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
	
	public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem)
    {
        if (droppedItem == null)
        {
            return null;
        }
        else if (droppedItem.stackSize == 0)
        {
            return null;
        }
        else
        {
            double d0 = this.posY - 0.30000001192092896D + (double)this.getEyeHeight();
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
            entityitem.setPickupDelay(40);

            if (traceItem)
            {
                entityitem.setThrower(this.getName());
            }

            float f;
            float f1;

            if (dropAround)
            {
                f = this.rand.nextFloat() * 0.5F;
                f1 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                entityitem.motionX = (double)(-MathHelper.sin(f1) * f);
                entityitem.motionZ = (double)(MathHelper.cos(f1) * f);
                entityitem.motionY = 0.20000000298023224D;
            }
            else
            {
                f = 0.3F;
                entityitem.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
                entityitem.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
                entityitem.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * f + 0.1F);
                f1 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                f = 0.02F * this.rand.nextFloat();
                entityitem.motionX += Math.cos((double)f1) * (double)f;
                entityitem.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double)f1) * (double)f;
            }

            this.worldObj.spawnEntityInWorld(entityitem);

            return entityitem;
        }
    }
	
	@Override
	public void onDeath(DamageSource cause)
    {
		super.onDeath(cause);
		if(!worldObj.isRemote)
		{
			this.inventory.dropAllItems();
		}
    }
	
	public void pickUpItem(EntityItem item)
	{
		if (!this.worldObj.isRemote)
		{
            if (item.cannotPickup()) 
            {
            	return;
            }

            ItemStack itemStack = item.getEntityItem();
            int size = itemStack.stackSize;
            
            if(inventory.addItemStackToInventory(itemStack))
            {
            	System.out.println("Added");
            	if (!item.isSilent())
                {
                    this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }

                this.onItemPickup(this, size);

                if (itemStack.stackSize <= 0)
                {
                    item.setDead();
                }
            }
		}
	}
}
