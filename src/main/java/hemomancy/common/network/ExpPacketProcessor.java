package hemomancy.common.network;

import hemomancy.common.util.LevelHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ExpPacketProcessor implements IMessage, IMessageHandler<ExpPacketProcessor, IMessage>
{
	private int curExp;
	private int expToNext;
	private int level;

    public ExpPacketProcessor()
    {
    }

    public ExpPacketProcessor(int curExp, int expToNext, int level)
    {
    	this.curExp = curExp;
    	this.expToNext = expToNext;
    	this.level = level;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(curExp);
        buffer.writeInt(expToNext);
        buffer.writeInt(level);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        curExp = buffer.readInt();
        expToNext = buffer.readInt();
        level = buffer.readInt();
    }

    @Override
    public IMessage onMessage(ExpPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.CLIENT)
    		message.syncPlayer(Minecraft.getMinecraft().thePlayer);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void syncPlayer(EntityPlayer player)
    {
    	LevelHandler.setCurrentExp(player, curExp);
    	LevelHandler.setExpToNext(player, expToNext);
    	LevelHandler.setLevel(player, level);
    }
}
