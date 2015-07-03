package hemomancy.common.util;

import hemomancy.api.ApiUtils;
import hemomancy.api.harvest.HarvestRegistry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Utils extends ApiUtils
{
	public static Random rand = new Random();
	public static void setLevel(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "Level", amount);
	}
	
	public static void setExpToNext(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "NextExp", amount);
	}
	
	public static void setCurrentExp(EntityPlayer player, int amount)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setInteger(IDTag + "CurExp", amount);
	}
	
	public static boolean isExpSynced(EntityPlayer player)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		if(data.hasKey(IDTag + "IsExpSynced"))
		{
			return data.getBoolean(IDTag + "IsExpSynced");
		}
		
		return false;
	}
	
	public static void setExpSynced(EntityPlayer player, boolean bool)
	{
		NBTTagCompound data = ApiUtils.getPersistentDataTag(player);
		data.setBoolean(IDTag + "IsExpSynced", bool);
	}
	
	public static boolean freezeBlock(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if(block == Blocks.water || block == Blocks.flowing_water)
		{
			return world.setBlockState(pos, Blocks.ice.getDefaultState(), 3);
		}
		
		return false;
	}
	
	public static boolean freezeBlocksInSphere(World world, BlockPos pos, int radius)
	{
		return freezeBlocksInSphere(world, pos, radius, 0, 0);
	}
	
	/**
	 * Freezes the blocks in a radius around the block position. 
	 * @param world
	 * @param pos
	 * @param radius
	 * @param featheringChance 	The chance that the particular block within the feathering depth will not actually freeze.
	 * @param featheringDepth	The depth from the outside where the sphere has a chance to feather
	 * @return
	 */
	public static boolean freezeBlocksInSphere(World world, BlockPos pos, int radius, float featheringChance, float featheringDepth)
	{
        boolean hasPlacedBlock = false;

		for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                    {
                        continue;
                    }
                    
                    if(i * i + j * j + k * k >= (radius + 0.50f - featheringDepth) * (radius + 0.50f - featheringDepth) && rand.nextFloat() < featheringChance)
                    {
                    	continue;
                    }
                    
                    if(freezeBlock(world, pos.add(i, j, k)))
                    {
                    	hasPlacedBlock = true;
                    }
                }
            }
        }
		
		return hasPlacedBlock;
	}
	
	public static boolean digBlock(World world, BlockPos pos, IBlockState state, int harvestLevel, int fortune, boolean silkTouch)
	{
		Block block = state.getBlock();
		
		int hlvl = block.getHarvestLevel(state);
		
		if(hlvl > harvestLevel || block.getBlockHardness(world, pos) < 0)
		{
			return false;
		}
		
		block.dropBlockAsItem(world, pos, state, 0);
		world.setBlockState(pos, Blocks.air.getDefaultState(), 3);
		
		return true;
	}
	
	public static boolean smeltBlocksInSphere(World world, BlockPos pos, int radius, float featheringChance, float featheringDepth, boolean dropIfItem)
	{
        boolean hasPlacedBlock = false;

		for (int i = -radius; i <= radius; i++)
        {
            for (int j = -radius; j <= radius; j++)
            {
                for (int k = -radius; k <= radius; k++)
                {
                    if (i * i + j * j + k * k >= (radius + 0.50f) * (radius + 0.50f))
                    {
                        continue;
                    }
                    
                    if(i * i + j * j + k * k >= (radius + 0.50f - featheringDepth) * (radius + 0.50f - featheringDepth) && rand.nextFloat() < featheringChance)
                    {
                    	continue;
                    }
                    
                    if(smeltBlockInWorld(world, pos.add(i, j, k), dropIfItem))
                    {
                    	hasPlacedBlock = true;
                    }
                }
            }
        }
		
		return hasPlacedBlock;
	}
	
	public static boolean smeltBlockInWorld(World world, BlockPos pos, boolean dropIfItem)
	{
		if(!world.isAirBlock(pos))
		{
			IBlockState state = world.getBlockState(pos);
			ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
			ItemStack itemStack = ItemStack.copyItemStack(FurnaceRecipes.instance().getSmeltingResult(blockStack));
			
			if(itemStack != null)
			{
				if(itemStack.getItem() instanceof ItemBlock)
				{
					Block newBlock = ((ItemBlock)itemStack.getItem()).getBlock();
					IBlockState newState = newBlock.getStateFromMeta(itemStack.getItemDamage());
					
					world.setBlockState(pos, newState);
					
					return true;
				}else if(dropIfItem)
				{
					world.spawnEntityInWorld(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack));
					world.setBlockToAir(pos);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Gives a bonemeal growth effect to the plant in question. Does not work on BlockBush because they appear to pop off on tall pushes.
	 * @param world
	 * @param pos
	 * @return
	 */
	public static boolean growPlantAtBlock(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		if(block instanceof IGrowable && !(block instanceof BlockBush))
		{
			((IGrowable)block).grow(world, rand, pos, state);
			
//			block.updateTick(world, pos, state, world.rand);
			
			return true;
		}
		
		return false;
	}
		
	public static boolean harvestPlantAtBlock(World world, BlockPos pos, boolean replantSeed)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		return HarvestRegistry.harvestBlock(world, block, state, pos, replantSeed);
	}
	
	public static void dropItemsAtPosition(World world, double x, double y, double z, List<ItemStack> dropList)
	{
		for (ItemStack item : dropList)
        {
            if (item != null && item.stackSize > 0)
            {
                float rx = rand.nextFloat() * 0.8F - 0.4F;
                float ry = rand.nextFloat() * 0.8F - 0.4F;
                float rz = rand.nextFloat() * 0.8F - 0.4F;
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

                if (item.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
	}
	
	public static boolean spawnEarthPillarAtPoint(World world, BlockPos pos, EnumFacing sideHit, int height, float radius, float featheringChance, float featheringDepth)
	{
		if(height <= 0)
		{
			return false;
		}
		
		boolean success = false;
		
		int intRadius = (int)Math.ceil(radius);
		for(int i = 0; i <= height; i++)
		{
			float effectiveRadius = radius - i * radius / (float)height;
			
			for(int j = -intRadius; j <= intRadius; j++)
			{
				for(int k = -intRadius; k <= intRadius; k++)
				{					
					if (j * j + k * k >= (effectiveRadius + 0.50f) * (effectiveRadius + 0.50f))
                    {
                        continue;
                    }
                    
                    if(j * j + k * k >= (effectiveRadius + 0.50f - featheringDepth) * (effectiveRadius + 0.50f - featheringDepth) && rand.nextFloat() < featheringChance)
                    {
                    	continue;
                    }
                    
					//This is "rotation" logic to rotate the block based on the facing hit
					BlockPos newPos = pos.add(sideHit.getFrontOffsetX() != 0 ? (i * sideHit.getFrontOffsetX()) : (sideHit.getFrontOffsetZ() != 0 ? k : j), sideHit.getFrontOffsetY() != 0 ? i * sideHit.getFrontOffsetY() : j, sideHit.getFrontOffsetZ() != 0 ? (i * sideHit.getFrontOffsetZ()) : (k));
					
					if(world.isAirBlock(newPos))
					{
						world.setBlockState(newPos, Blocks.stone.getDefaultState());
						
						success = true;
					}
				}
			}
		}
		
		return success;
	}
	
	public static List<BlockPos> getPillarBlocksForPoint(BlockPos pos, EnumFacing facing, int height, float radius, float featheringChance, float featheringDepth)
	{
		List<BlockPos> posList = new LinkedList();
		
		if(height <= 0)
		{
			return posList;
		}
				
		int intRadius = (int)Math.ceil(radius);
		for(int i = 0; i <= height; i++)
		{
			float effectiveRadius = radius - i * radius / (float)height;
			
			for(int j = -intRadius; j <= intRadius; j++)
			{
				for(int k = -intRadius; k <= intRadius; k++)
				{					
					if (j * j + k * k >= (effectiveRadius + 0.50f) * (effectiveRadius + 0.50f))
                    {
                        continue;
                    }
                    
                    if(j * j + k * k >= (effectiveRadius + 0.50f - featheringDepth) * (effectiveRadius + 0.50f - featheringDepth) && rand.nextFloat() < featheringChance)
                    {
                    	continue;
                    }
                    
					//This is "rotation" logic to rotate the block based on the facing hit
					BlockPos newPos = pos.add(facing.getFrontOffsetX() != 0 ? (i * facing.getFrontOffsetX()) : (facing.getFrontOffsetZ() != 0 ? k : j), facing.getFrontOffsetY() != 0 ? i * facing.getFrontOffsetY() : j, facing.getFrontOffsetZ() != 0 ? (i * facing.getFrontOffsetZ()) : (k));
					
					posList.add(newPos);
				}
			}
		}
		
		return posList;
	}
	
	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean useLiquids)
    {
        return getMovingObjectPositionFromPlayer(world, player, useLiquids, 5.0d, false);
    }
	
	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean useLiquids, double distance, boolean ignorePlayerBlockReach)
	{
		return getMovingObjectPositionFromPlayer(world, player, useLiquids, distance, ignorePlayerBlockReach, false);
	}
	
	public static MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean useLiquids, double distance, boolean ignorePlayerBlockReach, boolean searchForEntities)
	{
		float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
        float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
        double d0 = player.prevPosX + (player.posX - player.prevPosX);
        double d1 = player.prevPosY + (player.posY - player.prevPosY) + (double)player.getEyeHeight();
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ);
        Vec3 vec3 = new Vec3(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = distance;
        if (!ignorePlayerBlockReach && player instanceof net.minecraft.entity.player.EntityPlayerMP)
        {
            d3 = ((net.minecraft.entity.player.EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        MovingObjectPosition mop = world.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
        
        if(searchForEntities)
        {
        	double searchDistance = d3;
        	if(mop != null && mop.typeOfHit.equals(MovingObjectPosition.MovingObjectType.BLOCK))
        	{        		
        		double newDistance = mop.hitVec.distanceTo(vec3);
        		
        		if(newDistance < searchDistance)
        		{
        			searchDistance = newDistance;
        		}
        	}
        	
        	for(double i = 0; i < searchDistance; i++)
        	{
        		double hitX = vec3.xCoord + i*f6;
        		double hitY = vec3.yCoord + i*f5;
        		double hitZ = vec3.zCoord + i*f7;
        		
        		Entity closestEntity = null;
        		double closestDistance = searchDistance;
        		
        		List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(hitX - 0.5, hitY - 0.5, hitZ - 0.5, hitX + 0.5, hitY + 0.5, hitZ + 0.5));
        		
        		for(Entity entity : entityList)
        		{
        			double testDistance = entity.getPositionVector().distanceTo(vec3);
        			if(testDistance < closestDistance)
        			{
            			closestEntity = entity;
            			closestDistance = testDistance;
        			}
        		}
        		
        		if(closestEntity != null)
        		{
        			return new MovingObjectPosition(closestEntity);
        		}
        	}
        }
        
        return mop;
	}
	
	public static boolean disrobeEntity(EntityLivingBase hitEntity, float chance, int armourSlot)
	{
		if(hitEntity == null)
		{
			return false;
		}
		
		if(rand.nextFloat() < chance)
		{
			ItemStack armourStack = hitEntity.getEquipmentInSlot(armourSlot + 1);
			
			if(armourStack == null)
			{
				List<ItemStack> itemList = new ArrayList();
				itemList.add(armourStack);
				
				Utils.dropItemsAtPosition(hitEntity.worldObj, hitEntity.posX, hitEntity.posY, hitEntity.posZ, itemList);
				hitEntity.setCurrentItemOrArmor(armourSlot + 1, null);
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean disarmEntity(EntityLivingBase hitEntity, float chance)
	{
		if(hitEntity == null)
		{
			return false;
		}
		
		if(hitEntity instanceof EntityPlayer) //TODO If the player has an open gui, return false.
		{
			
		}
		
		if(rand.nextFloat() < chance)
		{
			ItemStack heldStack = hitEntity.getEquipmentInSlot(0);
			
			if(heldStack == null)
			{
				List<ItemStack> itemList = new ArrayList();
				itemList.add(heldStack);
				
				Utils.dropItemsAtPosition(hitEntity.worldObj, hitEntity.posX, hitEntity.posY, hitEntity.posZ, itemList);
				hitEntity.setCurrentItemOrArmor(0, null);
				
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isWithinRangeOfBlock(Entity entity, BlockPos pos, double range)
	{
		return pos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) < range*range;
	}
	
	public static Entity getEntityForUUID(World world, UUID id)
	{
		if(world instanceof WorldServer)
		{
			return ((WorldServer)world).getEntityFromUuid(id);
		}
		
		return null;
	}
}
