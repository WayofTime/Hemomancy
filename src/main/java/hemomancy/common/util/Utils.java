package hemomancy.common.util;

import hemomancy.api.ApiUtils;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
}
