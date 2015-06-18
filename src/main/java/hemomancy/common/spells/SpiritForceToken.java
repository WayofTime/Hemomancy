package hemomancy.common.spells;

import hemomancy.api.spells.*;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.SpiritForceProjectileEffect;

import java.util.List;

public class SpiritForceToken extends SpellToken implements IProjectileToken, IElementalToken
{
    public SpiritForceToken()
    {
        super("textures/tokens/SpiritForceToken.png");
        this.setUnlocalizedName("token.tokenSpiritForce.name");
    }

    @Override
    public EnumElement getElement()
    {
        return EnumElement.SPIRIT;
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
        focus.damageModifierList.add(new SpiritForceProjectileEffect(potency));
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof IElementalToken ? ((IElementalToken) token).getElement().equals(this.getElement()) : true;
    }

    @Override
    public SpellToken copy()
    {
        return new SpiritForceToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken focus, float potency)
    {
        return 3 * potency * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken focus, float potency)
    {
        return 7 * potency * potency;
    }
}
