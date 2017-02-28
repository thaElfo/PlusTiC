package landmaster.plustic.net;

import landmaster.plustic.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PlusTiC.MODID);
	
	public static void init() {
		INSTANCE.registerMessage(PacketReleaseEntity.class, PacketReleaseEntity.class, 0, Side.SERVER);
	}
}
