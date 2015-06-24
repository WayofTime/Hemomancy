package hemomancy.api.spells;

import hemomancy.common.spells.focus.SelfFocusToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISelfToken
{
    public boolean applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency);

    public int getSelfTokenUseDurationMinimum();
}
