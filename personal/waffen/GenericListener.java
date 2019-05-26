

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
// import java.util.Random;
import java.util.Random;

public class GenericListener {

	@SubscribeEvent
	public void onHandleInput(HandleInputEvent event) {
		try {
			int ch = (int) ReflectionHelper.findMethod(RSApplet.class, Modifier.FINAL, Integer.TYPE, "readChar", new Class[] {Integer.TYPE}).invoke(event.client, -796);
			String inputString = (String) ReflectionHelper.findField(client.class, Modifier.PRIVATE, String.class, "inputString").get(event.client);
			if ((client.openInterfaceID == -1 || client.openInterfaceID != ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "reportAbuseInterfaceID").getInt(event.client)) && ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "backDialogID").getInt(event.client) == -1 && (ch == 13 || ch == 10) && inputString.length() > 0 && inputString.startsWith(".")) {
				inputString = inputString.substring(1);
				String splitInputString = inputString.split(" ")[0];
				if (!splitInputString.isEmpty()) {
					for (Command c : Command.COMMANDS) {
						if (c.getUsage().split(" ")[0].equals(splitInputString)) {
							event.setCancelled(true);
							EventManager.callEvent(new ExecuteCommandEvent(inputString));
							break;
						}
					}
					if (!event.isCancelled()) {
						event.setCancelled(true);
						CommandUtils.pushCommandMessage("Console: inputted command '" + inputString + "' is invalid", CommandMessageTypeEnum.ERROR);
					}
				}
			}
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
	/* @SubscribeEvent
	public void onWriteString(WriteStringEvent event) {
		if (client.uberClient.loggingIn && event.str.equals(CreateUID.getMac())) {
			Random random = new Random();
			byte[] randomBytes = new byte[6];
			for (int i = 0; i < randomBytes.length; i++) {
				randomBytes[i] = (byte) random.nextInt(128);
			}
			String newStr = "";
			for (int i = 0; i < randomBytes.length; i++) {
				String s = "";
				if (i != randomBytes.length - 1) {
					s = "-";
				}
				newStr = newStr + String.format("%02X%s", new Object[] {randomBytes[i], s});
			}
			event.str = newStr;
		}
	} */
	@SubscribeEvent
	public void onLogin(LoginEvent event) {
		Random random = new Random();
		byte[] randomBytes = new byte[6];
		for (int i = 0; i < randomBytes.length; i++) {
			randomBytes[i] = (byte) random.nextInt(128);
		}
		client.checkMac = false;
		client.mac = randomBytes;
		try {
			client.macAdd = (int) ReflectionHelper.findMethod(client.class, Modifier.PRIVATE | Modifier.STATIC, Integer.TYPE, "byteArrayToInt", new Class[] {byte[].class, Integer.TYPE}).invoke(null, randomBytes, 0);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
