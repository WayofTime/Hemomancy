package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.common.spells.effect.LifeDrainEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.SelfFocusToken;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LifeDrainToken extends SpellToken implements IProjectileToken, ISelfToken, IBeamToken, IElementalToken
{
    public LifeDrainToken()
    {
        super("textures/tokens/HealToken.png");
        setUnlocalizedName("token.tokenLifeDrain.name");
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
    	focus.afterHitEffects.add(new LifeDrainEffect(potency));
    }

    @Override
    public boolean applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency)
    {        
    	return false;
    }

    @Override
    public SpellToken copy()
    {
        return new LifeDrainToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken focus, float potency)
    {
        return 2 * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken focus, float potency)
    {
        return 3 * potency;
    }

    @Override
    public int getSelfTokenUseDurationMinimum()
    {
        return 60;
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof IElementalToken ? ((IElementalToken) token).getElement().equals(this.getElement()) : true;
    }
    
	@Override
	public EnumElement getElement() 
	{
		return EnumElement.SPIRIT;
	}

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.afterHitEffects.add(new LifeDrainEffect(potency));
	}
}
