package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ITouchToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.api.spells.summon.ISummonToken;
import hemomancy.common.spells.beam.BlockBreakBlockBeamEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.SummonFocusToken;
import hemomancy.common.spells.focus.TouchFocusToken;
import hemomancy.common.spells.projectile.BlockBreakProjectileEffect;
import hemomancy.common.spells.summon.DigBlockManipulator;
import hemomancy.common.spells.touch.BlockBreakTouchEffect;

import java.util.List;

public class BlockBreakToken extends SpellToken implements IProjectileToken, ITouchToken, IBeamToken, ISummonToken, IElementalToken
{
	public BlockBreakToken() 
	{
		super("textures/tokens/DigToken.png");
		this.setUnlocalizedName("token.tokenBlockBreak.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new BlockBreakProjectileEffect(potency));
	}
	
	@Override
	public void manipulateTouchFocus(TouchFocusToken focus, float potency) 
	{
		focus.blockEffects.add(new BlockBreakTouchEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new BlockBreakToken();
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

	@Override
	public void manipulateSummonFocus(SummonFocusToken token, float potency) 
	{
		System.out.println("Added dig manipulator");
		token.blockManipulatorList.add(new DigBlockManipulator(potency));
	}
}