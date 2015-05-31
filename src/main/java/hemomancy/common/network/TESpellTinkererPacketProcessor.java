package hemomancy.common.network;

import hemomancy.common.blocks.tileEntity.TESpellTinkerer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;


public class TESpellTinkererPacketProcessor implements IMessage, IMessageHandler<TESpellTinkererPacketProcessor, IMessage>
{
	private ItemStack stack;
	private int dimension;
	private BlockPos pos;

    public TESpellTinkererPacketProcessor()
    {
    }

    public TESpellTinkererPacketProcessor(ItemStack stack, BlockPos pos, World world)
    {
    	this.stack = stack;
    	this.pos = pos;
    	this.dimension = world.provider.getDimensionId();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
    	PacketBuffer buff = new PacketBuffer(buffer);
    	buff.writeInt(dimension);
    	buff.writeBlockPos(pos);
    	buff.writeBoolean(stack != null);
    	
    	if(stack != null)
    	{
            buff.writeNBTTagCompoundToBuffer(stack.writeToNBT(new NBTTagCompound()));
    	}
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
    	PacketBuffer buff = new PacketBuffer(buffer);
    	dimension = buff.readInt();
    	pos = buff.readBlockPos();
    	boolean isStackThere = buff.readBoolean();
    	if(isStackThere)
    	{
    		try {
				stack = ItemStack.loadItemStackFromNBT(buff.readNBTTagCompoundFromBuffer());
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
    	}else
    	{
    		stack = null;
    	}
    }

    @Override
    public IMessage onMessage(TESpellTinkererPacketProcessor message, MessageContext ctx)
    {
    	if(ctx.side == Side.SERVER)
    	{
        	message.onMessageFromClient();
    	}
        return null;
    }
    
    public void onMessageFromClient()
    {
    	World world = DimensionManager.getWorld(dimension);
    	if(world != null)
    	{
    		TileEntity tile = world.getTileEntity(pos);
    		if(tile instanceof TESpellTinkerer)
    		{
    			((TESpellTinkerer) tile).setInventorySlotContents(0, stack);
    		}
    	}
    }
}
