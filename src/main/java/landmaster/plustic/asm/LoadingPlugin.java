package landmaster.plustic.asm;

import java.util.*;

import net.minecraftforge.fml.relauncher.*;

@IFMLLoadingPlugin.MCVersion(value = "1.10.2")
@IFMLLoadingPlugin.TransformerExclusions({"landmaster.plustic.asm"})
public class LoadingPlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {Transform.class.getName()};
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
