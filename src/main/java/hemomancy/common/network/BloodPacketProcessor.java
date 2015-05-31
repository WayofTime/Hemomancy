package hemomancy.common.network;

import hemomancy.api.ApiUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BloodPacketProcessor implements IMessage, IMessageHandler<BloodPacketProcessor, IMessage>
{
    private float blood;
    private float maxBlood;

    public BloodPacketProcessor()
    {
    }

    public BloodPacketProcessor(float blood, float maxBlood)
    {
        this.blood = blood;
        this.maxBlood = maxBlood;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeFloat(blood);
        buffer.writeFloat(maxBlood);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        blood = buffer.readFloat();
        maxBlood = buffer.readFloat();
    }

    @Override
    public IMessage onMessage(BloodPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.CLIENT)
    		message.syncPlayer(Minecraft.getMinecraft().thePlayer);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void syncPlayer(EntityPlayer player)
    {
    	ApiUtils.setClientBloodOfPlayer(player, blood);
    	ApiUtils.setMaxClientBloodOfPlayer(player, maxBlood);
    }
}
