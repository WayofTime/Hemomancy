package hemomancy.common.spells;

import hemomancy.api.spells.EnumElement;
import hemomancy.api.spells.IElementalToken;
import hemomancy.api.spells.IFocusToken;
import hemomancy.api.spells.IProjectileToken;
import hemomancy.api.spells.SpellToken;
import hemomancy.common.spells.projectile.UndeadTurnProjectileEffect;

import java.util.List;

public class UndeadTurnToken extends SpellToken implements IProjectileToken, IElementalToken
{
    public UndeadTurnToken()
    {
        super("textures/tokens/TurnUndeadToken.png");
        this.setUnlocalizedName("token.tokenUndeadTurn.name");
    }

    @Override
    public EnumElement getElement()
    {
        return EnumElement.SPIRIT;
    }

    @Override
    public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency)
    {
    	focus.onCollideEffectList.add(new UndeadTurnProjectileEffect(potency));
    }

    @Override
    public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
    {
        return token instanceof IElementalToken ? ((IElementalToken) token).getElement().equals(this.getElement()) : true;
    }

    @Override
    public SpellToken copy()
    {
        return new UndeadTurnToken();
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
