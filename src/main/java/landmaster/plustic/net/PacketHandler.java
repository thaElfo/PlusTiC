package landmaster.plustic.net;

import landmaster.plustic.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PlusTiC.MODID);
	
	public static void init() {
		int msgInd = 0;
		INSTANCE.registerMessage(PacketReleaseEntity.class, PacketReleaseEntity.class, msgInd++, Side.SERVER);
		INSTANCE.registerMessage(PacketHandleToggleGui.class, PacketHandleToggleGui.class, msgInd++, Side.SERVER);
		INSTANCE.registerMessage(PacketUpdateToggleGui.class, PacketUpdateToggleGui.class, msgInd++, Side.CLIENT);
	}
}
