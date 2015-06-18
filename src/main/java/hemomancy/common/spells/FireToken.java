package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.FireProjectileEffect;

import java.util.List;

public class FireToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public FireToken() 
	{
		super("textures/tokens/FireToken.png");
		this.setUnlocalizedName("token.tokenFire.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.damageMap.put("fire", (double) (5 * potency));
		focus.damageModifierList.add(new FireProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new FireToken();
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
