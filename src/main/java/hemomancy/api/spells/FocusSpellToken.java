package hemomancy.api.spells;

public abstract class FocusSpellToken extends SpellToken implements IFocusToken
{
	public FocusSpellToken(String location) 
	{
		super(location);
	}
	
	public boolean isSpellTokenCompatible(SpellToken token)
	{
		return token instanceof IFocusToken ? token.key.equals(this.key) : false;
	}
}
