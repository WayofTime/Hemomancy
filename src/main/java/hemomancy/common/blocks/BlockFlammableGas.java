package hemomancy.common.blocks;

import hemomancy.Hemomancy;
import hemomancy.common.blocks.tileEntity.TEFlammableGas;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlammableGas extends BlockContainer
{
	public BlockFlammableGas() 
	{
		super(Material.circuits);
		setHardness(2.0F);
        setResistance(5.0F);
        this.setUnlocalizedName("blockFlammableGas");
        setCreativeTab(Hemomancy.tabHemomancy);
    }
	
	@Override
	public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion explosion)
    {
        if (!world.isRemote)
        {
        	world.createExplosion(explosion.getExplosivePlacedBy(), pos.getX(), pos.getY(), pos.getZ(), 3, false);
        }
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	public int getRenderType()
    {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TEFlammableGas();
    }
}
