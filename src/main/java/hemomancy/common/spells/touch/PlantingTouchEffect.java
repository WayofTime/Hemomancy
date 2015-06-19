package hemomancy.common.spells.touch;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class PlantingTouchEffect implements IClickBlockTouchEffect
{
	public float potency;
	
	public PlantingTouchEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean clickBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, Block block, EnumFacing sideHit) 
	{				
		int radius = this.getRadiusOfPlanting(potency);
		
		boolean success = false;
		
		List<Integer> slotPositionList = new ArrayList();
		ItemStack[] inv = player.inventory.mainInventory;
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
						ItemStack stack = player.inventory.mainInventory[slot];
						if(stack == null || !(stack.getItem() instanceof IPlantable))
						{
							slotPositionList.remove(slot);
							continue;
						}
						
						IPlantable seed = (IPlantable)stack.getItem();
						
						if (!player.canPlayerEdit(newPos.up(), EnumFacing.UP, stack))
				        {
				            continue;
				        }
				        else if (newBlock.canSustainPlant(world, newPos, EnumFacing.UP, seed) && world.isAirBlock(newPos.up()))
				        {
				            world.setBlockState(newPos.up(), seed.getPlant(world, newPos.up()));
				            success = true;
				            
				            if(!player.capabilities.isCreativeMode)
				            {
				            	stack.stackSize--;
					            
					            if(stack.stackSize <= 0)
								{
									player.inventory.mainInventory[slot] = null;
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
		return potency;
	}
}
