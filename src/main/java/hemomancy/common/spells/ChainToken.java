package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;

public class ChainToken extends SpellToken implements IProjectileToken
{

	public ChainToken() 
	{
		super("textures/tokens/TestingToken.png");
		this.setUnlocalizedName("token.tokenChain.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.chainAttackNumber += (int)(2 * potency);
	}

	@Override
	public SpellToken copy() 
	{
		return new ChainToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 10 * potency;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 40 * potency;
	}
}
