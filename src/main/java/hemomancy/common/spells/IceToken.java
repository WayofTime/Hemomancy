package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.beam.IBeamToken;
import hemomancy.common.spells.beam.IceBlockBeamEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.IceProjectileEffect;

import java.util.List;

public class IceToken extends SpellToken implements IProjectileToken, IBeamToken, IElementalToken
{
	public IceToken() 
	{
		super("textures/tokens/IceToken.png");
		this.setUnlocalizedName("token.tokenIce.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new IceProjectileEffect(potency));
		focus.damageMap.put("iceAttack", (double) (5 * potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new IceToken();
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
