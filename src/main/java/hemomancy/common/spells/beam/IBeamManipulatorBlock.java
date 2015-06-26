package hemomancy.common.spells.beam;

import hemomancy.common.spells.focus.BeamFocusToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBeamManipulatorBlock 
{
	/**
	 * Does an effect based on the beam that hits it.
	 * @param focus
	 * @param player
	 * @return true if the beam will not perform the block effect on this block
	 */
	public boolean manipulateBeam(BeamFocusToken focus, World world, BlockPos pos, EntityPlayer player);
}
