package hemomancy.common.network;

import hemomancy.api.mana.ManaHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ManaPacketProcessor implements IMessage, IMessageHandler<ManaPacketProcessor, IMessage>
{
    private float mana;
    private int maxMana;

    public ManaPacketProcessor()
    {
    	
    }

    public ManaPacketProcessor(float mana, int maxMana)
    {
        this.mana = mana;
        this.maxMana = maxMana;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeFloat(mana);
        buffer.writeInt(maxMana);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        mana = buffer.readFloat();
        maxMana = buffer.readInt();
    }

    @Override
    public IMessage onMessage(ManaPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.CLIENT)
    		message.syncPlayer(Minecraft.getMinecraft().thePlayer);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void syncPlayer(EntityPlayer player)
    {
    	ManaHandler.setManaOfPlayer(player, mana);
    	ManaHandler.setMaxManaOfPlayer(player, maxMana);
    }
}
