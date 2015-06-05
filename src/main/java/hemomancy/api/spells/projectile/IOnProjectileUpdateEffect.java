package hemomancy.api.spells.projectile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IOnProjectileUpdateEffect 
{
	public boolean onProjectileUpdate(Entity projectile, EntityPlayer shooter, float potency);
	
	public boolean onProjectileStickyUpdate(Entity projectile, EntityPlayer shooter, BlockPos pos, IBlockState state, EnumFacing sideHit, int ticksInGround, float potency);
}
