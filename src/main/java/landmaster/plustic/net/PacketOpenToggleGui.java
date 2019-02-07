package landmaster.plustic.net;

import java.util.*;

import io.netty.buffer.*;
import landmaster.plustic.api.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketOpenToggleGui implements IMessage {
	private Collection<String> abilities;
	
	public PacketOpenToggleGui() {}
	public PacketOpenToggleGui(Collection<String> abilities) {
		this.abilities = abilities;
	}
	
	public static IMessage onMessage(PacketOpenToggleGui message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().displayGuiScreen(new Toggle.Gui(Minecraft.getMinecraft().player, message.abilities));
		});
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int sz = buf.readInt();
		abilities = new ArrayList<>(sz);
		for (int i=0; i<sz; ++i) abilities.add(ByteBufUtils.readUTF8String(buf));
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(abilities.size());
		for (String ability: abilities) ByteBufUtils.writeUTF8String(buf, ability);
	}
	
}
