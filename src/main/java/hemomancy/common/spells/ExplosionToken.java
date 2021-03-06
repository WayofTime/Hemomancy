package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
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
		focus.onCollideEffectList.add(new ExplosionProjectileEffect(potency));
		focus.onUpdateEffectList.add(new ExplosionProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new ExplosionToken();
	}

	@Override
	public float getBloodCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 5 * potency*potency;
	}

	@Override
	public float getManaCostOfToken(IFocusToken token, float potency) 
	{
		// TODO Auto-generated method stub
		return 20 * potency*potency;
	}
}
