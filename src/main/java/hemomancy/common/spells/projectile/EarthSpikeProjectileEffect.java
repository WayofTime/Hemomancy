package hemomancy.common.spells.projectile;

import hemomancy.api.spells.projectile.IOnProjectileCollideEffect;
import hemomancy.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EarthSpikeProjectileEffect implements IOnProjectileCollideEffect
{
	public float potency;
	
	public EarthSpikeProjectileEffect(float potency)
	{
		this.potency = potency;
	}
	
	@Override
	public boolean onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity) 
	{
		return false;
	}

	@Override
	public boolean onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		BlockPos newPos = pos.add(sideHit.getDirectionVec());

		return spawnEarthSpikeAtBlock(projectile.worldObj, newPos, sideHit, potency);
	}

	@Override
	public boolean onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit) 
	{
		BlockPos newPos = pos.add(sideHit.getDirectionVec());

		return spawnEarthSpikeAtBlock(projectile.worldObj, newPos, sideHit, potency / 3f);
	}

	public boolean spawnEarthSpikeAtBlock(World world, BlockPos pos, EnumFacing sideHit, float potency)
	{
		Utils.spawnEarthPillarAtPoint(world, pos, sideHit, 10, 5, 0.3f, 1);
		
		return true;
	}
	
	@Override
	public float getPotency() 
	{
		return this.potency;
	}

}