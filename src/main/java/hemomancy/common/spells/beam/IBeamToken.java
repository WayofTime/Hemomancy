package hemomancy.common.spells.beam;

import hemomancy.common.spells.focus.BeamFocusToken;

public interface IBeamToken 
{
	public void manipulateBeamFocus(BeamFocusToken focus, float potency);
}
