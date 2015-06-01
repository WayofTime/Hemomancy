package hemomancy.common.spells;

import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;

public class BounceToken extends SpellToken implements IProjectileToken
{

	public BounceToken() 
	{
		super("textures/tokens/BounceToken.png");
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
}
