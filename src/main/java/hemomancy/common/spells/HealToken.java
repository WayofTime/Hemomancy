package hemomancy.common.spells;

import java.util.List;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.ISelfToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.focus.SelfFocusToken;
import hemomancy.common.spells.projectile.HealProjectileEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class HealToken extends SpellToken implements IProjectileToken, ISelfToken, IElementalToken
{
    public HealToken()
    {
        super("textures/tokens/HealToken.png");
        setUnlocalizedName("token.tokenHeal.name");
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
        focus.dealDamage = false;
        focus.onCollideEffectList.add(new HealProjectileEffect(potency));
    }

    @Override
    public boolean applyEffectToPlayer(World world, EntityPlayer player, SelfFocusToken focus, float potency)
    {
    	if(player.getHealth() < player.getMaxHealth())
    	{
    		player.heal(5 * potency);
    		return true;
    	}
        
    	return false;
    }

    @Override
    public SpellToken copy()
    {
        return new HealToken();
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
}
