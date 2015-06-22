package hemomancy.common.blocks.tileEntity;

import hemomancy.ModBlocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TEFlammableGas extends TileEntity implements IUpdatePlayerListBox
{
	public int duration = 200;
	public int originalDuration = 200;
	
	public int sizeLimit = 3;
	
	public final float decayFactor = 0.5f;
	public final float spreadChance = 0.1f;
	public final float dropChance = 0.6f;
	
	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setInteger("duration", duration);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.duration = tag.getInteger("duration");
        this.originalDuration = tag.getInteger("originalDuration");
        
        this.sizeLimit = tag.getInteger("sizeLimit");
    }

	@Override
	public void update() 
	{
		if(worldObj.isRemote)
		{
			return;
		}
		
		duration--;
		
//		List<EntityLivingBase> entityList = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.pos, this.pos.add(1, 1, 1)));
//		
//		for(EntityLivingBase living : entityList)
//		{
//			living.attackEntityFrom(DamageSource.inFire, 5);
//		}
		
		if(sizeLimit > 0 && worldObj.rand.nextFloat() <= spreadChance)
		{
			spread();
		}
		
		if(duration <= 0)
		{
			worldObj.setBlockToAir(getPos());
		}
	}
	
	public void spread()
	{
		List<EnumFacing> directionList = new ArrayList();
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			if(worldObj.isAirBlock(getPos().add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ())))
			{
				directionList.add(facing);
			}
		}
		
		if(!directionList.isEmpty())
		{
			EnumFacing facing = directionList.get(worldObj.rand.nextInt(directionList.size()));
			
			TEFlammableGas.createFlammableBlock(worldObj, getPos().add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()), (int) (originalDuration * decayFactor), sizeLimit - (worldObj.rand.nextFloat() < this.dropChance ? 1 : 0));
		}
	}
	
	public static void createFlammableBlock(World world, BlockPos pos, int duration, int sizeLimit)
	{
		world.setBlockState(pos, ModBlocks.blockFlammableGas.getDefaultState());
		TEFlammableGas plasmaBlock = (TEFlammableGas)world.getTileEntity(pos);
		
		plasmaBlock.duration = duration;
		plasmaBlock.originalDuration = duration;
		plasmaBlock.sizeLimit = sizeLimit;
	}
}
