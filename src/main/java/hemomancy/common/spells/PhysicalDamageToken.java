package hemomancy.common.spells;

import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ITouchToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.TouchFocusToken;
import hemomancy.common.spells.general.PhysicalCustomDamageSource;

public class PhysicalDamageToken extends SpellToken implements IProjectileToken, IBeamToken, ITouchToken
{
    public PhysicalDamageToken()
    {
        super("textures/tokens/PhysicalDamageToken.png");
        this.setUnlocalizedName("token.tokenPhysicalDamage.name");
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
    	focus.damageMap.put("physical", potency * 5d);
    }

    @Override
    public SpellToken copy()
    {
        return new PhysicalDamageToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken token, float potency)
    {
        return 0;
    }

    @Override
    public float getManaCostOfToken(IFocusToken token, float potency)
    {
        return 0;
    }

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.damageMap.put("physical", potency * 2f);
		focus.damageSources.add(new PhysicalCustomDamageSource());
	}

	@Override
	public void manipulateTouchFocus(TouchFocusToken focus, float potency) 
	{
		focus.damageMap.put("physical", potency * 5f);
		focus.damageSources.add(new PhysicalCustomDamageSource());
	}
}
