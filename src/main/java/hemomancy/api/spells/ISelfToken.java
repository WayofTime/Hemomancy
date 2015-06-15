package hemomancy.api.spells;

import hemomancy.common.spells.SelfFocusToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISelfToken
{
    public void applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency);

    public int getSelfTokenUseDuration();
}
