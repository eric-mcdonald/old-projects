package net.hackforums.pootpoot.monumental.mode;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IChatComponent;

public interface Mode {

	List<Mode> MODES = new ArrayList<Mode>();
	boolean bypassesNoCheatPlus();
	IChatComponent getDescription();
	KeyBinding getKeyBind();
	IChatComponent getName();
	boolean isEnabled();
	void onDisable();
	void onEnable();
	void setEnabled(boolean enabled);
	void setKeyBind(KeyBinding keyBind);
}
