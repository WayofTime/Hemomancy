package hemomancy.client.gui.entity;

import hemomancy.client.gui.entity.controlButtons.SummonControlButton;

import java.util.HashMap;

public class ControlButtonRegistry 
{
	public static HashMap<String, SummonControlButton> buttonMap = new HashMap();
	
	public void registerButton(String str, SummonControlButton button)
	{
		buttonMap.put(str, button);
	}
	
	
}
