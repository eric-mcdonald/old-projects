

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class SpawnObjectCommand extends CommandBase {

	public SpawnObjectCommand() {
		super("Spawn Object", "Allows the player to spawn client-sided objects", "so <id>");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(String command) {
		// TODO Auto-generated method stub
		String[] splitCommand = command.split(" ");
		if (splitCommand.length == 2) {
			try {
				int id = Integer.parseInt(splitCommand[1]);
				if (id < 0) {
					CommandUtils.pushCommandMessage(this.getName() + ": changing ID from " + id + " to " + (id = 0), CommandMessageTypeEnum.WARNING);
				}
				int x = (ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseX").getInt(client.uberClient) + (client.myPlayer.x - 6 >> 7)) - ((ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "anInt1069").getInt(client.uberClient) - 6) * 8), y = (ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseY").getInt(client.uberClient) + (client.myPlayer.y - 6 >> 7)) - ((ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "anInt1070").getInt(client.uberClient) - 6) * 8);
				if (y > 0 && y < 103 && x > 0 && x < 103) {
					ReflectionHelper.findMethod(client.class, Modifier.PRIVATE, Void.TYPE, "method130", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE}).invoke(client.uberClient, -1, id, 0, ((int[]) ReflectionHelper.findField(client.class, Modifier.PRIVATE | Modifier.FINAL, int[].class, "anIntArray1177").get(client.uberClient))[40 >> 2], y, 10, 0, x, 0);
					CommandUtils.pushCommandMessage(this.getName() + ": spawned the object with ID " + id + " at the player's location", CommandMessageTypeEnum.INFO);
				}
				else {
					CommandUtils.pushCommandMessage(this.getName() + ": failed to spawn the object with ID " + id + " at the player's location", CommandMessageTypeEnum.INFO);
				}
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
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
