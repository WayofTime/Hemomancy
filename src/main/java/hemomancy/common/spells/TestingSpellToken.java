package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.SpellToken;

public class TestingSpellToken extends SpellToken
{
	public TestingSpellToken()
	{
		super("textures/tokens/TestingToken.png");
	}

	@Override
	public SpellToken copy() 
	{
		return new TestingSpellToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken focus, float potency) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getManaCostOfToken(IFocusToken focus, float potency) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
