package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;

public class StickyToken extends SpellToken implements IProjectileToken
{
	public StickyToken() 
	{
		super("textures/tokens/StickyToken.png");
		this.setUnlocalizedName("token.tokenSticky.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.stickTime = 10 * 20; //10 seconds
	}

	@Override
	public SpellToken copy() 
	{
		return new StickyToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken focus) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getManaCostOfToken(IFocusToken focus) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
