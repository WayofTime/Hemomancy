package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.BlockBreakProjectileEffect;

import java.util.List;

public class BlockBreakToken extends SpellToken implements IProjectileToken, IElementalToken
{
	public BlockBreakToken() 
	{
		super("textures/tokens/TestingToken.png");
		this.setUnlocalizedName("token.tokenBlockBreak.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new BlockBreakProjectileEffect(potency));
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
	public float getBloodCostOfToken(IFocusToken token) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token) 
	{
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.EARTH;
	}
}