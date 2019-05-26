

import java.lang.reflect.Modifier;

// import com.gunrun66.waffen.util.ReflectionHelper;

public class PrivilegeCommand extends CommandBase {

	public PrivilegeCommand() {
		super("Privilege", "Sets the player's permissions", "privilege <permissions>");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String command) {
		// TODO Auto-generated method stub
		String[] splitCommand = command.split(" ");
		if (splitCommand.length == 2) {
			try {
				int permissions = Integer.parseInt(splitCommand[1]);
				if (permissions < 0 || permissions > 5) {
					CommandUtils.pushCommandMessage(this.getName() + ": changing permissions from " + permissions + " to " + (permissions = permissions < 0 ? 0 : 5), CommandMessageTypeEnum.WARNING);
				}
				ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "myPrivilege").setInt(client.uberClient, permissions); // client.uberClient.myPrivilege = permissions;
				CommandUtils.pushCommandMessage(this.getName() + ": permissions have been set to " + permissions, CommandMessageTypeEnum.INFO);
			}
			catch (NumberFormatException e) {
				CommandUtils.pushCommandMessage(this.getName() + ": usage: " + this.getUsage(), CommandMessageTypeEnum.ERROR);
				return;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
