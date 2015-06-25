package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.common.spells.beam.IceBlockBeamEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.IcicleDropProjectileEffect;

import java.util.List;

public class IcicleDropToken extends SpellToken implements IProjectileToken, IBeamToken, IElementalToken
{
	public IcicleDropToken() 
	{
		super("textures/tokens/IceToken.png");
		this.setUnlocalizedName("token.tokenIcicle.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new IcicleDropProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new IcicleDropToken();
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
		return EnumElement.ICE;
	}

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.blockEffects.add(new IceBlockBeamEffect(potency));
	}
}
