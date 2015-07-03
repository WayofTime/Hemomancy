package hemomancy.common.network;

import hemomancy.common.summon.SummonHandler;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class SummonSyncPacketProcessor implements IMessage, IMessageHandler<SummonSyncPacketProcessor, IMessage>
{
    private UUID summonId;
    private String playerString;

    public SummonSyncPacketProcessor()
    {
    }

    public SummonSyncPacketProcessor(UUID summonId, String playerString)
    {
        this.summonId = summonId;
        this.playerString = playerString;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {        
        if(buffer instanceof PacketBuffer)
        {
        	((PacketBuffer)buffer).writeUuid(summonId);
        	((PacketBuffer)buffer).writeString(playerString);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        if(buffer instanceof PacketBuffer)
        {
        	summonId = ((PacketBuffer)buffer).readUuid();
        	playerString = ((PacketBuffer)buffer).readStringFromBuffer(50);
        }
    }

    @Override
    public IMessage onMessage(SummonSyncPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.CLIENT)
    	{
    		message.syncSummon();
    	}
    	
        return null;
    }
    
    public void syncSummon()
    {
    	SummonHandler.registerSummonToPlayer(playerString, summonId);
    }
}
