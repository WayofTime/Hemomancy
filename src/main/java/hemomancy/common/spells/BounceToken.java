package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;

public class BounceToken extends SpellToken implements IProjectileToken
{

	public BounceToken() 
	{
		super("textures/tokens/BounceToken.png");
		this.setUnlocalizedName("token.tokenBounce.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.bouncesLeft += 5;
	}

	@Override
	public SpellToken copy() 
	{
		return new BounceToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
