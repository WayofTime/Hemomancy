package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.GrowthProjectileEffect;

import java.util.List;

public class GrowthToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public GrowthToken() 
	{
		super("textures/tokens/GrowthToken.png");
		this.setUnlocalizedName("token.tokenGrowth.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new GrowthProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new GrowthToken();
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
		return 35 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.NATURE;
	}
}