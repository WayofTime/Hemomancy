package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ITouchToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.TouchFocusToken;
import hemomancy.common.spells.projectile.HarvestProjectileEffect;
import hemomancy.common.spells.touch.HarvestTouchEffect;

import java.util.List;

public class HarvestToken extends SpellToken implements IProjectileToken, ITouchToken, IElementalToken
{
	public HarvestToken() 
	{
		super("textures/tokens/HarvestToken.png");
		this.setUnlocalizedName("token.tokenHarvest.name");
	}
	
	@Override
	public void manipulateTouchFocus(TouchFocusToken focus, float potency) 
	{
		focus.blockEffects.add(new HarvestTouchEffect(potency));
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new HarvestProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new HarvestToken();
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