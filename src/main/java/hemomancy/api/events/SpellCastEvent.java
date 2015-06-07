package hemomancy.api.events;

import hemomancy.api.spells.SpellToken;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellCastEvent extends Event
{
	public final EntityPlayer player;
	public final List<SpellToken> tokenList;
	public final float potency;
	
	public SpellCastEvent(EntityPlayer player, List<SpellToken> tokenList, float potency) 
	{
		super();
		this.player = player;
		this.tokenList = tokenList;
		this.potency = potency;
	}	
}