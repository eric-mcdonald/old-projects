


public class CommandListener {

	@SubscribeEvent
	public void onExecuteCommand(ExecuteCommandEvent event) {
		for (Command c : Command.COMMANDS) {
			if (c.getUsage().split(" ")[0].equals(event.command.split(" ")[0])) {
				c.execute(event.command);
				break;
			}
		}
	}
}
