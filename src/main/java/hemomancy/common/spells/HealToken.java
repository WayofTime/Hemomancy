package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class HealToken extends SpellToken implements IProjectileToken, ISelfToken
{
    public HealToken()
    {
        super("textures/tokens/TestingToken.png");
        setUnlocalizedName("token.tokenHeal.name");
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
        focus.dealDamage = false;
        focus.damageMap.put("heal", (double) -1 * potency);
    }

    @Override
    public void applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency)
    {
        player.heal(potency);
    }

    @Override
    public SpellToken copy()
    {
        return new HealToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken focus, float potency)
    {
        return 2 * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken focus, float potency)
    {
        return 3 * potency;
    }

    @Override
    public int getSelfTokenUseDuration()
    {
        return 5;
    }
}
