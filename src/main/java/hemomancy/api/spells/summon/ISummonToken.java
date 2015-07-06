package hemomancy.api.spells.summon;

import hemomancy.common.spells.focus.SummonFocusToken;

public interface ISummonToken 
{
	public void manipulateSummonFocus(SummonFocusToken token, float potency);
}
