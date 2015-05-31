package hemomancy.common.network;

import hemomancy.api.spells.SpellTokenRegistry;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class SpellTokenPacketProcessor implements IMessage, IMessageHandler<SpellTokenPacketProcessor, IMessage>
{
	private NBTTagCompound tag;

    public SpellTokenPacketProcessor()
    {
    }

    public SpellTokenPacketProcessor(EntityPlayer player)
    {
    	this(SpellTokenRegistry.getTokenTagOfPlayer(player));
    }
    
    public SpellTokenPacketProcessor(NBTTagCompound tag)
    {
    	this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
    	PacketBuffer buff = new PacketBuffer(buffer);

        buff.writeNBTTagCompoundToBuffer(tag);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
    	PacketBuffer buff = new PacketBuffer(buffer);

    	try {
			tag = buff.readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public IMessage onMessage(SpellTokenPacketProcessor message, MessageContext ctx)
    {
    	if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
    	{
        	message.onMessageFromClient();
    	}
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void onMessageFromClient()
    {
    	SpellTokenRegistry.setTokenTagOfPlayer(Minecraft.getMinecraft().thePlayer, tag); 
    }
}
