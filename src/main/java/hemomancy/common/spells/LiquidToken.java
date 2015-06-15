package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;

public class LiquidToken extends SpellToken implements IProjectileToken
{

    public LiquidToken()
    {
        super("textures/tokens/LiquidPassToken.png");
        this.setUnlocalizedName("token.tokenLiquid.name");
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
        focus.collideWithFluids = false;
    }

    @Override
    public SpellToken copy()
    {
        return new LiquidToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 2 * potency * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken token, float potency)
    {
        // TODO Auto-generated method stub
        return 3 * potency * potency;
    }
}
