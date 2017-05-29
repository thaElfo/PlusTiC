package landmaster.plustic.tools.stats;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.client.resources.*;
import slimeknights.tconstruct.library.materials.*;

public class LaserMediumMaterialStats extends AbstractMaterialStats {
	public static final String TYPE = "laser_medium";
	
	public final float power;
	
	static {
		Material.UNKNOWN.addStats(new LaserMediumMaterialStats(0));
	}
	
	public LaserMediumMaterialStats(float power) {
		super(TYPE);
		this.power = power;
	}
	
	@Override
	public List<String> getLocalizedInfo() {
		return ImmutableList.of(I18n.format("stat.laser_medium.power.name", power));
	}

	@Override
	public List<String> getLocalizedDesc() {
		return ImmutableList.of(I18n.format("stat.laser_medium.power.desc"));
	}
}
