package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.beam.IBeamToken;
import hemomancy.common.spells.beam.PoisonEntityBeamEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.PoisonProjectileEffect;

import java.util.List;

public class PoisonToken extends SpellToken implements IProjectileToken, IBeamToken, IElementalToken
{
	public PoisonToken() 
	{
		super("textures/tokens/PoisonToken.png");
		this.setUnlocalizedName("token.tokenPoison.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new PoisonProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new PoisonToken();
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
		return 3 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.POISON;
	}

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.entityEffects.add(new PoisonEntityBeamEffect(potency));
	}
}
