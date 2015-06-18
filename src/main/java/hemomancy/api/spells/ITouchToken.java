package hemomancy.api.spells;

import hemomancy.common.spells.focus.TouchFocusToken;

public interface ITouchToken 
{
	public void manipulateTouchFocus(TouchFocusToken focus, float potency);
}
