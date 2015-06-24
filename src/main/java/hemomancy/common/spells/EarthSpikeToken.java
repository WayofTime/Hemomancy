package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ITouchToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.beam.BlockBreakBlockBeamEffect;
import hemomancy.common.spells.beam.IBeamToken;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.TouchFocusToken;
import hemomancy.common.spells.projectile.EarthSpikeProjectileEffect;
import hemomancy.common.spells.touch.BlockBreakTouchEffect;

import java.util.List;

public class EarthSpikeToken extends SpellToken implements IProjectileToken, ITouchToken, IBeamToken, IElementalToken
{
	public EarthSpikeToken() 
	{
		super("textures/tokens/EarthSpikeToken.png");
		this.setUnlocalizedName("token.tokenEarthSpike.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new EarthSpikeProjectileEffect(potency));
	}
	
	@Override
	public void manipulateTouchFocus(TouchFocusToken focus, float potency) 
	{
		focus.blockEffects.add(new BlockBreakTouchEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new EarthSpikeToken();
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
		return 5 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.EARTH;
	}

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.ignoreEntities = true;
		focus.blockEffects.add(new BlockBreakBlockBeamEffect(potency));
	}
}