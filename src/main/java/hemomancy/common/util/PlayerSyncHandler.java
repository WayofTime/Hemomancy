package hemomancy.common.util;

import hemomancy.api.IPlayerSyncHandler;
import hemomancy.api.mana.BloodHandler;
import hemomancy.api.mana.ManaHandler;
import hemomancy.common.network.BloodPacketProcessor;
import hemomancy.common.network.ExpPacketProcessor;
import hemomancy.common.network.ManaPacketProcessor;
import hemomancy.common.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerSyncHandler implements IPlayerSyncHandler
{
	@Override
	public void syncPlayerMana(EntityPlayerMP player) 
	{
		PacketHandler.INSTANCE.sendTo(new ManaPacketProcessor(ManaHandler.getManaOfPlayer(player), ManaHandler.getMaxManaOfPlayer(player)), player);
	}

	@Override
	public void syncPlayerBlood(EntityPlayerMP player) 
	{
		PacketHandler.INSTANCE.sendTo(new BloodPacketProcessor(BloodHandler.getBloodOfPlayer(player), BloodHandler.getMaxBloodOfPlayer(player)), player);
	}

	@Override
	public void syncPlayerExp(EntityPlayerMP player) 
	{
		PacketHandler.INSTANCE.sendTo(new ExpPacketProcessor(Utils.getCurrentExp(player), Utils.getExpToNext(player), Utils.getLevel(player)), player);
	}
}
