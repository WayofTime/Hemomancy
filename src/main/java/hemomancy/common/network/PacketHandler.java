package hemomancy.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


public class PacketHandler  //Server-to-client packet handler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Hemomancy");

    public static void init()
    {
    	INSTANCE.registerMessage(ManaPacketProcessor.class, ManaPacketProcessor.class, 0, Side.CLIENT);
    	INSTANCE.registerMessage(BloodPacketProcessor.class, BloodPacketProcessor.class, 1, Side.CLIENT);
    	INSTANCE.registerMessage(ExpPacketProcessor.class, ExpPacketProcessor.class, 2, Side.CLIENT);
    	INSTANCE.registerMessage(TESpellTinkererPacketProcessor.class, TESpellTinkererPacketProcessor.class, 3, Side.SERVER);
    	INSTANCE.registerMessage(SpellTokenPacketProcessor.class, SpellTokenPacketProcessor.class, 4, Side.CLIENT);
    }
}
