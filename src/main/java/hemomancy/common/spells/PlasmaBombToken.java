package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.PlasmaBombProjectileEffect;

import java.util.List;

public class PlasmaBombToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public PlasmaBombToken() 
	{
		super("textures/tokens/PlasmaBombToken.png");
		this.setUnlocalizedName("token.tokenPlasmaBomb.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
//		focus.damageMap.put("fire", (double) (5 * potency));
		focus.onCollideEffectList.add(new PlasmaBombProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new PlasmaBombToken();
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
		return 75 * potency*potency;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 150 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.PLASMA;
	}
}
