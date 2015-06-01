package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.ExplosionProjectileEffect;

public class ExplosionToken extends SpellToken implements IProjectileToken
{
	public ExplosionToken() 
	{
		super("textures/tokens/ExplosionToken.png");
		this.setUnlocalizedName("token.tokenExplosion.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new ExplosionProjectileEffect());
		focus.onUpdateEffectList.add(new ExplosionProjectileEffect());
	}

	@Override
	public SpellToken copy() 
	{
		return new ExplosionToken();
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
		return 20;
	}
}
