package hemomancy.client.gui.entity;

import java.util.List;

import net.minecraft.util.ResourceLocation;

public class SummonControlButton 
{
	public ResourceLocation location;
	
	public SummonControlButton(String location)
	{
		this(new ResourceLocation("hemomancy", location));
	}
	
	public SummonControlButton(ResourceLocation location)
	{
		this.location = location;
	}
	
	public ResourceLocation getResourceLocation()
	{
		return location;
	}
	
	public boolean onButtonClicked()
	{
		return false;
	}
	
	public String getLocalizedName()
	{
		return "Name";
	}
	
	public void getHoverText(List<String> list)
	{
		list.add("Button!");
	}
}
