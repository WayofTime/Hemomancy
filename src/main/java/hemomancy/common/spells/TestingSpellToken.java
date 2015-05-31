package hemomancy.common.spells;

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
}
