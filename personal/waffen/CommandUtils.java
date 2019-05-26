


public class CommandUtils {

	public static void pushCommandMessage(String message, CommandMessageTypeEnum type) {
		client.uberClient.pushMessage("[" + Waffen.NAME + "] [" + type.getName() + "] " + message, 0, "");
	}
}
