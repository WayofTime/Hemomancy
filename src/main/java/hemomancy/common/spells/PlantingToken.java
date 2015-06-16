package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.PlantingProjectileEffect;

import java.util.List;

public class PlantingToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public PlantingToken() 
	{
		super("textures/tokens/PlantToken.png");
		this.setUnlocalizedName("token.tokenPlanting.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new PlantingProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new PlantingToken();
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