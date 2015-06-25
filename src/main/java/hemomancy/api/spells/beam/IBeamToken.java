package hemomancy.api.spells.beam;

import hemomancy.common.spells.focus.BeamFocusToken;

public interface IBeamToken 
{
	public void manipulateBeamFocus(BeamFocusToken focus, float potency);
}
