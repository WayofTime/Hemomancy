package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.FireProjectileEffect;

public class FireToken extends SpellToken implements IProjectileToken
{
	public FireToken() 
	{
		super("textures/tokens/FireToken.png");
		this.setUnlocalizedName("token.tokenFire.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.damageModifierList.add(new FireProjectileEffect());
	}

	@Override
	public SpellToken copy() 
	{
		return new FireToken();
	}
	
	@Override
	public boolean isSpellTokenCompatible(SpellToken token)
	{
		return !(token instanceof IceToken);
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
