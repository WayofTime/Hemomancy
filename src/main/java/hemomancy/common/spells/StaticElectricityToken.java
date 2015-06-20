package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.StaticElectricityProjectileEffect;

import java.util.List;

public class StaticElectricityToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public StaticElectricityToken() 
	{
		super("textures/tokens/StaticToken.png");
		this.setUnlocalizedName("token.tokenStatic.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new StaticElectricityProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new StaticElectricityToken();
	}
	
	@Override
	public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
	{
		return token instanceof IElementalToken ? ((IElementalToken)token).getElement().equals(this.getElement()) : true;
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		return 3 * potency*potency;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		return 5 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.ELECTRICITY;
	}
}
