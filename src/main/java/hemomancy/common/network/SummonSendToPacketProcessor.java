package hemomancy.common.network;

import hemomancy.common.entity.mob.EntitySummon;
import hemomancy.common.summon.SummonHandler;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class SummonSendToPacketProcessor implements IMessage, IMessageHandler<SummonSendToPacketProcessor, IMessage>
{
    private UUID summonId;
    private String playerString;
    private BlockPos pos;

    public SummonSendToPacketProcessor()
    {
    	
    }

    public SummonSendToPacketProcessor(UUID summonId, String playerString, BlockPos pos)
    {
        this.summonId = summonId;
        this.playerString = playerString;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {        
    	System.out.println("Sent: " + summonId);
        if(buffer instanceof PacketBuffer)
        {
        	((PacketBuffer)buffer).writeUuid(summonId);
        	((PacketBuffer)buffer).writeString(playerString);
        	((PacketBuffer)buffer).writeBlockPos(pos);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
    	PacketBuffer newBuffer = new PacketBuffer(buffer);

    	summonId = newBuffer.readUuid();
    	playerString = newBuffer.readStringFromBuffer(50);
    	pos = newBuffer.readBlockPos();
    	System.out.println("Recieved: " + summonId);
    }

    @Override
    public IMessage onMessage(SummonSendToPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.SERVER)
    	{
    		message.syncSummon(ctx.getServerHandler().playerEntity.worldObj);
    	}
    	
        return null;
    }
    
    public void syncSummon(World world)
    {
    	EntitySummon summon = SummonHandler.getSummonEntity(world, summonId);
    	    	
    	if(summon != null)
    	{
        	System.out.println("I have recieved the packet");

    		summon.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0);
    	}
    }
}
