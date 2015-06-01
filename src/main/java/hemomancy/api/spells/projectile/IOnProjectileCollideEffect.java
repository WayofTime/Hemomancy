package hemomancy.api.spells.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IOnProjectileCollideEffect 
{
	public void onProjectileHitEntity(Entity projectile, EntityPlayer shooter, EntityLivingBase hitEntity, float potency);
	
	public void onProjectileCollideWithBlock(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency);
	
	public void onProjectileBounce(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, float potency);
}
