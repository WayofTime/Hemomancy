package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.DisrobeProjectileEffect;

import java.util.List;

public class DisrobeToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public DisrobeToken() 
	{
		super("textures/tokens/DisrobeToken.png");
		this.setUnlocalizedName("token.tokenDisrobe.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new DisrobeProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new DisrobeToken();
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
		return 1 * potency*potency;
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
		return EnumElement.FIRE;
	}
}