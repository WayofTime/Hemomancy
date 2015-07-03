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


public class SummonBlockPosPacketProcessor implements IMessage, IMessageHandler<SummonBlockPosPacketProcessor, IMessage>
{
	public static final int SEND_TO = 0;
	public static final int MARK_IDLE_LOCATION = 1;
	public static final int DEFINE_WORK_AREA = 2;
	
	private int packetType;
    private UUID summonId;
    private String playerString;
    private BlockPos pos;

    public SummonBlockPosPacketProcessor()
    {
    	
    }

    public SummonBlockPosPacketProcessor(int packetType, UUID summonId, String playerString, BlockPos pos)
    {
    	this.packetType = packetType;
        this.summonId = summonId;
        this.playerString = playerString;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {        
    	PacketBuffer newBuffer = new PacketBuffer(buffer);

    	newBuffer.writeInt(packetType);
    	newBuffer.writeUuid(summonId);
    	newBuffer.writeString(playerString);
    	newBuffer.writeBlockPos(pos);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
    	PacketBuffer newBuffer = new PacketBuffer(buffer);

    	packetType = newBuffer.readInt();
    	summonId = newBuffer.readUuid();
    	playerString = newBuffer.readStringFromBuffer(50);
    	pos = newBuffer.readBlockPos();
    }

    @Override
    public IMessage onMessage(SummonBlockPosPacketProcessor message, MessageContext ctx)
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
    	    	
    	if(summon != null && pos != null)
    	{
    		switch(packetType)
    		{
    		case SEND_TO:
        		summon.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1.0);
        		break;
        		
    		case MARK_IDLE_LOCATION:
    			if(!summon.isWithinWorkArea(pos))
    			{
    				summon.workArea = false;
    				summon.startingBlockArea = new BlockPos(0, 0, 0);
    				summon.endingBlockArea = new BlockPos(0, 0, 0);
    				summon.isIdle = true;
    				summon.startWasLastSet = false;
    				summon.workAreaWasReset = true;
    			}
    			
    			summon.idleLocation = pos;
    			break;
    			
    		case DEFINE_WORK_AREA:
    			if(!summon.startWasLastSet)
    			{
    				summon.startingBlockArea = pos;
    				summon.startWasLastSet = true;
    			}else
    			{
    				summon.endingBlockArea = pos;
    				summon.startWasLastSet = false;
    			}
    			
    			if(summon.workAreaWasReset)
    			{
    				if(!summon.startWasLastSet)
    				{
    					summon.workAreaWasReset = false;
    					summon.workArea = true;
    					
    					if(!summon.isWithinWorkArea(summon.idleLocation))
    					{
    						summon.idleLocation = summon.getCentralPositionInBlockArea();
    					}
    				}
    			}else if(!summon.isWithinWorkArea(summon.idleLocation))
				{
					summon.idleLocation = summon.getCentralPositionInBlockArea();
				}
    			
    			break;
    		}
    	}
    }
}
