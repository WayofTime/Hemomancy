package hemomancy.common.spells;

import hemomancy.api.spells.*;
import hemomancy.common.spells.focus.ProjectileFocusToken;
import hemomancy.common.spells.projectile.WaterProjectileEffect;

import java.util.List;

public class WaterToken extends SpellToken implements IProjectileToken, IElementalToken
{
    public WaterToken()
    {
        super("textures/tokens/WaterToken.png");
        this.setUnlocalizedName("token.tokenWater.name");
    }

    @Override
    public EnumElement getElement()
    {
        return EnumElement.WATER;
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
        focus.onCollideEffectList.add(new WaterProjectileEffect(potency));
        focus.damageMap.put("waterAttack", (double) (5 * potency));
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof IElementalToken ? ((IElementalToken) token).getElement().equals(this.getElement()) : true;
    }

    @Override
    public SpellToken copy()
    {
        return new WaterToken();
    }

    @Override
    public float getBloodCostOfToken(IFocusToken focus, float potency)
    {
        return 3 * potency * potency;
    }

    @Override
    public float getManaCostOfToken(IFocusToken focus, float potency)
    {
        return 5 * potency * potency;
    }
}
