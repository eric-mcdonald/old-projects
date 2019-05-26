package net.hackforums.pootpoot.monumental.mode.modes;

import net.hackforums.pootpoot.monumental.command.Command;
import net.hackforums.pootpoot.monumental.command.NoCheatPlusCompatible;
import net.hackforums.pootpoot.monumental.mode.BaseMode;
import net.hackforums.pootpoot.monumental.mode.Mode;

public class NoCheatPlusMode extends BaseMode {

	public NoCheatPlusMode() {
		super("NoCheatPlus", "Makes applicable modes bypass NoCheatPlus");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean bypassesNoCheatPlus() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		for (Command command : Command.COMMANDS) {
			if (command instanceof NoCheatPlusCompatible) {
				((NoCheatPlusCompatible) command).onNCPDisable();
			}
		}
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		super.onEnable();
		for (Mode mode : MODES) {
			if (!mode.bypassesNoCheatPlus()) {
				mode.setEnabled(false);
			}
		}
		for (Command command : Command.COMMANDS) {
			if (command instanceof NoCheatPlusCompatible) {
				((NoCheatPlusCompatible) command).onNCPEnable();
			}
		}
	}

}
