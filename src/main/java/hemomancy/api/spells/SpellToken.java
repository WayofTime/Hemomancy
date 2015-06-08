package hemomancy.api.spells;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class SpellToken
{
	public ResourceLocation location;
	public String key = ""; //This key is used for quick look-up in the SpellTokenRegistry for SpellToken -> key. It is set when the token is registered.
	private String unlocalizedName = "";
	
	private float potency = 0; 	//The potency of the spell token. This is set by the spell whenever the spell needs a specific potency. 
								//The potency is saved to the NBT of the spell, and is changed on a new cast.
	public SpellToken(String location)
	{
		this(new ResourceLocation("hemomancy", location));
	}
	
	public void setUnlocalizedName(String unlocName)
	{
		this.unlocalizedName = unlocName;
	}
	
	public String getLocalizedName()
	{
		return StatCollector.translateToLocal(unlocalizedName);
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
	
	public boolean isSpellTokenCompatible(List<SpellToken> tokenList, SpellToken token)
	{
		return true;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return this == o || (o instanceof SpellToken ? ((SpellToken)o).key.equals(key) : false);
	}
	
	public abstract float getBloodCostOfToken(IFocusToken focus, float potency);
	
	public abstract float getManaCostOfToken(IFocusToken focus, float potency);
	
	public float expForSituationSuccess(SpellSituation situation, float potency)
	{
		return 1;
	}
	
	public float getPotencyOfToken()
	{
		return this.potency;
	}
	
	public void setPotencyOfToken(float potency)
	{
		this.potency = potency;
	}
}
