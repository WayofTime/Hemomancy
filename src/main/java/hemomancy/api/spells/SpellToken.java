package hemomancy.api.spells;

import net.minecraft.util.ResourceLocation;

public abstract class SpellToken
{
	public ResourceLocation location;
	public String key = ""; //This key is used for quick look-up in the SpellTokenRegistry for SpellToken -> key. It is set when the token is registered.
	
	public SpellToken(String location)
	{
		this(new ResourceLocation("hemomancy", location));
	}
	
	public SpellToken(ResourceLocation location)
	{
		this.location = location;
	}
	
	public ResourceLocation getResourceLocation()
	{
		return this.location;
	}
	
	public abstract SpellToken copy();
	
	public boolean isSpellTokenCompatible(SpellToken token)
	{
		return true;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return this == o || (o instanceof SpellToken ? ((SpellToken)o).key.equals(key) : false);
	}
}
