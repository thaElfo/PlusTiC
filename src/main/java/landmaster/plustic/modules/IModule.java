package landmaster.plustic.modules;

import java.util.*;

import gnu.trove.set.hash.*;

// TODO retrofit modules to implement this
public interface IModule {
	public static final Set<IModule> modules = new THashSet<>();
	
	default void init() {}
	default void init2() {}
}
