package hemomancy.client.gui.entity;

import hemomancy.common.summon.SummonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerSummonControl extends Container
{
	public final EntityPlayer player;
	public final UUID summonId;
	
	public List<SummonControlButton> controlButtonList = new ArrayList();
	
	public ContainerSummonControl(EntityPlayer player)
	{
		super();
		this.player = player;
		this.summonId = SummonHandler.getActiveSummon(player);

		controlButtonList.add(new SummonControlButton("textures/tokens/BeamToken.png"));
		controlButtonList.add(new SummonControlButton("textures/tokens/BeamToken.png"));
		controlButtonList.add(new SummonControlButton("textures/tokens/BeamToken.png"));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}