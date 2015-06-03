package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.IceProjectileEffect;

public class IceToken extends SpellToken implements IProjectileToken
{
	public IceToken() 
	{
		super("textures/tokens/IceToken.png");
		this.setUnlocalizedName("token.tokenIce.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new IceProjectileEffect());
	}

	@Override
	public SpellToken copy() 
	{
		return new IceToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token) 
	{
		// TODO Auto-generated method stub
		return 4;
	}
}
