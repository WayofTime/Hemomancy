package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.SmeltBlockProjectileEffect;

import java.util.List;

public class FireSmeltToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public FireSmeltToken() 
	{
		super("textures/tokens/SmeltingToken.png");
		this.setUnlocalizedName("token.tokenFireSmelt.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new SmeltBlockProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new FireSmeltToken();
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