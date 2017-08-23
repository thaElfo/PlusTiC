package landmaster.plustic.modules;

import com.progwml6.natura.nether.*;
import com.progwml6.natura.nether.block.logs.*;
import com.progwml6.natura.nether.block.planks.*;

import net.minecraft.item.*;

public class ModuleNaturaStuff {
	public static ItemStack darkwoodPlankStack() {
		return new ItemStack(NaturaNether.netherPlanks,
			1, BlockNetherPlanks.PlankType.DARKWOOD.getMeta());
	}
	public static ItemStack darkwoodLogStack() {
		return new ItemStack(NaturaNether.netherLog,
			1, BlockNetherLog.LogType.DARKWOOD.getMeta());
	}
	public static ItemStack ghostwoodPlankStack() {
		return new ItemStack(NaturaNether.netherPlanks,
			1, BlockNetherPlanks.PlankType.GHOSTWOOD.getMeta());
	}
	public static ItemStack ghostwoodLogStack() {
		return new ItemStack(NaturaNether.netherLog,
			1, BlockNetherLog.LogType.GHOSTWOOD.getMeta());
	}
	public static ItemStack fusewoodPlankStack() {
		return new ItemStack(NaturaNether.netherPlanks,
			1, BlockNetherPlanks.PlankType.FUSEWOOD.getMeta());
	}
	public static ItemStack fusewoodLogStack() {
		return new ItemStack(NaturaNether.netherLog,
			1, BlockNetherLog.LogType.FUSEWOOD.getMeta());
	}
}
