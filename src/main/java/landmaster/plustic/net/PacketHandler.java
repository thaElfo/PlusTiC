package landmaster.plustic.net;

import landmaster.plustic.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PlusTiC.MODID);
	
	public static void init() {
		INSTANCE.registerMessage(PacketReleaseEntity::onMessage, PacketReleaseEntity.class, 0, Side.SERVER);
		INSTANCE.registerMessage(PacketHandleToggleGui::onMessage, PacketHandleToggleGui.class, 1, Side.SERVER);
		INSTANCE.registerMessage(PacketUpdateToggleGui::onMessage, PacketUpdateToggleGui.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(PacketSetPortal::onMessage, PacketSetPortal.class, 3, Side.SERVER);
		INSTANCE.registerMessage(PacketBrownAbracadabra::onMessage, PacketBrownAbracadabra.class, 4, Side.SERVER);
	}
}
