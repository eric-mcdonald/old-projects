

import java.util.ArrayList;
import java.util.List;

public interface Module {

	List<Module> MODULES = new ArrayList<Module>();
	String getDescription();
	KeyBinding getKeyBinding();
	String getName();
	ModuleTypeEnum getType();
	boolean isEnabled();
	void onDisable();
	void onEnable();
	void setKeyBinding(KeyBinding keyBinding);
	void toggle();
	void update();
}
