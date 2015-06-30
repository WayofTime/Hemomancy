package hemomancy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class SummonCommandPacketProcessor implements IMessage, IMessageHandler<SummonCommandPacketProcessor, IMessage>
{
    private int summonId;
    private String summonMessage;

    public SummonCommandPacketProcessor()
    {
    }
    
    public SummonCommandPacketProcessor(int summonId, String message)
    {
        this.summonId = summonId;
        this.summonMessage = message;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(summonId);
        buffer.writeInt(summonMessage.length());
        buffer.writeBytes(summonMessage.getBytes());
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        summonId = buffer.readInt();
        int size = buffer.readInt();
        String message = "";
        
        for(int i=0; i<size; i++)
        {
        	message = message + buffer.readChar();
        }
        
        summonMessage = message;
    }

    @Override
    public IMessage onMessage(SummonCommandPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.SERVER)
    	{
    		message.onMessageFromClient(Minecraft.getMinecraft().thePlayer);
    	}
    	
        return null;
    }
    
    public void onMessageFromClient(EntityPlayer player)
    {
    	World world = player.worldObj;
    	Entity entity = world.getEntityByID(summonId);
    	
    	
    }
}
