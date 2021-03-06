package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.blocks.tileEntity.TEFlammableGas;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FlameGasProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public FlameGasProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		BlockPos newPos = hitEntity.getPosition();
		
		return spawnGasAtBlock(hitEntity.worldObj, newPos, potency);
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		BlockPos newPos = pos.add(sideHit.getDirectionVec());

		return spawnGasAtBlock(projectile.worldObj, newPos, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		BlockPos newPos = pos.add(sideHit.getDirectionVec());

		return spawnGasAtBlock(projectile.worldObj, newPos, potency / 3f);
	}

	public boolean spawnGasAtBlock(World world, BlockPos pos, float potency)
	{
		TEFlammableGas.createFlammableBlock(world, pos, (int) potency * 200, (int) potency * 2);
		
		return true;
	}

	@Override
	public float getPotency() 
	{
		return potency;
	}

}
