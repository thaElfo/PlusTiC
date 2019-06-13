package landmaster.plustic.gui.container;

import landmaster.plustic.tile.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

public class ContainerTECentrifugeCore extends Container {
	protected final EntityPlayer player;
	protected final TECentrifugeCore te;
	
	public TECentrifugeCore getTE() {
		return te;
	}

	public ContainerTECentrifugeCore(EntityPlayer player, TECentrifugeCore te) {
		super();
		this.player = player;
		this.te = te;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return playerIn.getDistanceSq(
				this.te.getPos().getX() + 0.5,
				this.te.getPos().getY() + 0.5,
				this.te.getPos().getZ() + 0.5) <= 64;
	}
}
