package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;

import java.util.List;

public class PushToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public PushToken() 
	{
		super("textures/tokens/FireToken.png");
		this.setUnlocalizedName("token.tokenPush.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.knockbackStrength += potency * 2.0f;
	}

	@Override
	public SpellToken copy() 
	{
		return new PushToken();
	}
	
	@Override
	public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
	{
		return token instanceof IElementalToken ? ((IElementalToken)token).getElement().equals(this.getElement()) : true;
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 3 * potency*potency;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 7 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.AIR;
	}
}
