package hemomancy.api.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class LevelUpEvent extends Event
{
	public final EntityPlayer player;
	public final int nextLevel;
	
	public LevelUpEvent(EntityPlayer player, int nextLevel) 
	{
		super();
		this.player = player;
		this.nextLevel = nextLevel;
	}	
}
