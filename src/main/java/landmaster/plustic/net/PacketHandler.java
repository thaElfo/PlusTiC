package landmaster.plustic.net;

import landmaster.plustic.api.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
	
	public static void init() {
		INSTANCE.registerMessage(PacketReleaseEntity::onMessage, PacketReleaseEntity.class, 0, Side.SERVER);
		INSTANCE.registerMessage(PacketHandleToggleGui::onMessage, PacketHandleToggleGui.class, 1, Side.SERVER);
		INSTANCE.registerMessage(PacketUpdateToggleGui::onMessage, PacketUpdateToggleGui.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(PacketSetPortal::onMessage, PacketSetPortal.class, 3, Side.SERVER);
		INSTANCE.registerMessage(PacketBrownAbracadabra::onMessage, PacketBrownAbracadabra.class, 4, Side.SERVER);
		INSTANCE.registerMessage(PacketUpdateToolModeServer::onMessage, PacketUpdateToolModeServer.class, 5, Side.SERVER);
		INSTANCE.registerMessage(PacketLaserGunZapBlock::onMessage, PacketLaserGunZapBlock.class, 6, Side.CLIENT);
	}
}
