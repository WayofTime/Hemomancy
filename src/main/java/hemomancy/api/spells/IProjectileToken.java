package hemomancy.api.spells;

import hemomancy.common.spells.focus.ProjectileFocusToken;

public interface IProjectileToken 
{
	public void manipulateProjectileFocus(ProjectileFocusToken focus, float potency);
}
