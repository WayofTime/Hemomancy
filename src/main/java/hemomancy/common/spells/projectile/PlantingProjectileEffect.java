package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class PlantingProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public PlantingProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return this.growPlantsAtPoint(projectile, shooter, hitEntity.posX, hitEntity.posY, hitEntity.posZ, potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.growPlantsAtPoint(projectile, shooter, pos.getX(), pos.getY(), pos.getZ(), potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		return this.growPlantsAtPoint(projectile, shooter, pos.getX(), pos.getY(), pos.getZ(), potency / 2.0f);
	}
	
	private boolean growPlantsAtPoint(Entity projectile, EntityPlayer shooter, double x, double y, double z, float potency)
	{
		if(shooter == null)
		{
			return false;
		}
		
		World world = projectile.worldObj;
		int xPos = (int)x;
		int yPos = (int)y;
		int zPos = (int)z;
		
		BlockPos pos = new BlockPos(xPos, yPos, zPos);
		
		int radius = this.getRadiusOfPlanting(potency);
		
		boolean success = false;
		
		List<Integer> slotPositionList = new ArrayList();
		ItemStack[] inv = shooter.inventory.mainInventory;
		for(int i = 0; i < inv.length; i++)
		{
			ItemStack stack = inv[i];
			if(stack != null && stack.getItem() instanceof IPlantable)
			{

				slotPositionList.add(i);
			}
		}
		
		if(slotPositionList.isEmpty())
		{
			return success;
		}
		
		for(int i = -radius; i <= radius; i++)
		{
			for(int j = -radius; j <= radius; j++)
			{
				for(int k = -radius; k <= radius; k++)
				{
					BlockPos newPos = pos.add(i, j, k);
					IBlockState newState = world.getBlockState(newPos);
					Block newBlock = newState.getBlock();
					
					for(Integer slot : slotPositionList)
					{
						ItemStack stack = shooter.inventory.mainInventory[slot];
						if(stack == null || !(stack.getItem() instanceof IPlantable))
						{
							slotPositionList.remove(slot);
							continue;
						}
						
						IPlantable seed = (IPlantable)stack.getItem();
						
						if (!shooter.canPlayerEdit(newPos.up(), EnumFacing.UP, stack))
				        {
				            continue;
				        }
				        else if (newBlock.canSustainPlant(world, newPos, EnumFacing.UP, seed) && world.isAirBlock(newPos.up()))
				        {
				            world.setBlockState(newPos.up(), seed.getPlant(world, newPos.up()));
				            success = true;
				            
				            if(!shooter.capabilities.isCreativeMode)
				            {
				            	stack.stackSize--;
					            
					            if(stack.stackSize <= 0)
								{
									shooter.inventory.mainInventory[slot] = null;
									slotPositionList.remove(slot);
								}
				            }
				            
				            break;
				        }
					}
					
					if(slotPositionList.isEmpty())
					{
						return success;
					}
				}
			}
		}
		
		return success;
	}
	
	private int getRadiusOfPlanting(float potency)
	{
		return (int)(potency * potency);
	}
	
	@Override
	public float getPotency() 
	{
		return this.potency;
	}
}
