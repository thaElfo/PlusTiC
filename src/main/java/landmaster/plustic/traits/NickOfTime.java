package landmaster.plustic.traits;

import net.minecraft.init.*;

public class NickOfTime extends DeathSaveTrait {
	public static final NickOfTime nickOfTime = new NickOfTime();
	
	public NickOfTime() {
		super("nickoftime", 0xFFF98E, 8, stack -> stack != null && stack.getItem() == Items.ENDER_PEARL,
				"msg.plustic.nickmodifier.use");
	}
}
