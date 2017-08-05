package landmaster.plustic.modules;

import java.util.*;

// TODO retrofit modules to implement this
public interface IModule {
	public static final Set<IModule> modules = new LinkedHashSet<>();
	
	default void init() {}
	default void init2() {}
}
