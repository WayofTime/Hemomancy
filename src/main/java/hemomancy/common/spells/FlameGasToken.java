package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.FlameGasProjectileEffect;

import java.util.List;

public class FlameGasToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public FlameGasToken() 
	{
		super("textures/tokens/FlameGasToken.png");
		this.setUnlocalizedName("token.tokenFlameGas.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new FlameGasProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new FlameGasToken();
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
