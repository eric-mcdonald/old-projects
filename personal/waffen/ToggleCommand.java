


public class ToggleCommand extends CommandBase {

	public static boolean isKeyBindingsEnabled() {
		return keyBindingsEnabled;
	}
	private static boolean keyBindingsEnabled;

	public ToggleCommand() {
		super("Toggle", "Enables/disables various modifications", "t <modification>");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void execute(String command) {
		// TODO Auto-generated method stub
		String[] splitCommand = command.split(" ");
		if (splitCommand.length == 2) {
			for (Module m : Module.MODULES) {
				if (StringUtils.getToggleName(m.getName()).equals(splitCommand[1])) {
					m.toggle();
					CommandUtils.pushCommandMessage(this.getName() + ": modification " + m.getName() + " has been toggled", CommandMessageTypeEnum.INFO);
					return;
				}
			}
			if (splitCommand[1].equals("key_bindings")) {
				keyBindingsEnabled = !keyBindingsEnabled;
				CommandUtils.pushCommandMessage(this.getName() + ": modification Key Bindings has been toggled", CommandMessageTypeEnum.INFO);
				return;
			}
			CommandUtils.pushCommandMessage(this.getName() + ": modification " + splitCommand[1] + " is invalid", CommandMessageTypeEnum.ERROR);
		}
	}

}
