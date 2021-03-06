package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;

public class LiquidToken extends SpellToken implements IProjectileToken, IBeamToken
{

    public LiquidToken()
    {
        super("textures/tokens/PassThroughLiquidToken.png");
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
        return 0;
    }

    @Override
    public float getManaCostOfToken(IFocusToken token, float potency)
    {
        return 0;
    }

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.collideWithLiquids = false;
	}
}
