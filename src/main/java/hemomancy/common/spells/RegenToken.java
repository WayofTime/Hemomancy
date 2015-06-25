package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.api.spells.beam.IBeamToken;
import hemomancy.common.spells.beam.RegenEntityBeamEffect;
import hemomancy.common.spells.focus.BeamFocusToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.SelfFocusToken;
import hemomancy.common.spells.projectile.RegenProjectileEffect;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class RegenToken extends SpellToken implements IProjectileToken, IBeamToken, ISelfToken, IElementalToken
{
	public RegenToken() 
	{
		super("textures/tokens/RegenToken.png");
		this.setUnlocalizedName("token.tokenRegen.name");
	}

	@Override
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency) 
	{
		focus.onCollideEffectList.add(new RegenProjectileEffect(potency));
	}

	@Override
	public SpellToken copy() 
	{
		return new RegenToken();
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
		return 3 * potency*potency;
	}

	@Override
	public EnumElement getElement() 
	{
		return EnumElement.SPIRIT;
	}

	@Override
	public void manipulateBeamFocus(BeamFocusToken focus, float potency) 
	{
		focus.entityEffects.add(new RegenEntityBeamEffect(potency));
	}

	@Override
	public boolean applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency) 
	{
		PotionEffect eff = new PotionEffect(Potion.regeneration.id, (int)(300 * potency), Math.max((int)Math.floor(potency - 0.01), 0));
        if(player.isPotionApplicable(eff))
        {
        	player.addPotionEffect(eff);
        	
        	return true;
        }

		return false;
	}

	@Override
	public int getSelfTokenUseDurationMinimum() 
	{
		return 100;
	}
}