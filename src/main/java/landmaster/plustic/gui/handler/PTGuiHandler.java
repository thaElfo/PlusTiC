package landmaster.plustic.gui.handler;

import landmaster.plustic.gui.*;
import landmaster.plustic.gui.container.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.*;

public class PTGuiHandler implements IGuiHandler {
	public static final int FRUITSALAD = 0;
	public static final int MOTS = 1;
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FRUITSALAD:
			return new ContainerFruitSalad(player);
		case MOTS:
			return new ContainerMOTS(player);
		default:
			return null;
		}
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case FRUITSALAD:
			return new GuiFruitSalad(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
		case MOTS:
			return new GuiMOTS(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
		default:
			return null;
		}
	}
	
}
