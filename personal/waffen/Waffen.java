

// import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;

import sign.signlink;

public class Waffen {

	public static void main(String[] args) {
		try {
			/* ReflectionHelper.findField(client.class, Modifier.PRIVATE | Modifier.STATIC, Integer.TYPE, "nodeID").setInt(null, 10);
			ReflectionHelper.findField(client.class, Modifier.STATIC, Integer.TYPE, "portOff").setInt(null, 0);
			ReflectionHelper.findMethod(client.class, Modifier.PRIVATE | Modifier.STATIC, Void.TYPE, "setHighMem", new Class[0]).invoke(null);
			ReflectionHelper.findField(client.class, Modifier.PRIVATE | Modifier.STATIC, Boolean.TYPE, "isMembers").setBoolean(null, true);
			signlink.storeid = 32;
			signlink.startpriv(InetAddress.getLocalHost());
			client.uberClient = new ClientHook();
			ReflectionHelper.findMethod(RSApplet.class, Modifier.FINAL, Void.TYPE, "createClientFrame", new Class[] {Integer.TYPE, Integer.TYPE}).invoke(client.uberClient, 503, 765); */
			ReflectionHelper.findField(client.class, Modifier.PRIVATE | Modifier.STATIC, Integer.TYPE, "nodeID").setInt(null, 10);
			ReflectionHelper.findField(client.class, Modifier.STATIC, Integer.TYPE, "portOff").setInt(null, 0);
			ReflectionHelper.findMethod(client.class, Modifier.PRIVATE | Modifier.STATIC, Void.TYPE, "setHighMem", new Class[0]).invoke(null);
			ReflectionHelper.findField(client.class, Modifier.PRIVATE | Modifier.STATIC, Boolean.TYPE, "isMembers").setBoolean(null, true);
			client.clientHeight = 503;
			client.clientWidth = 765;
			signlink.storeid = 32;
			signlink.startpriv(InetAddress.getByName("recklesspk.zapto.org"));
			client.uberClient = new ClientHook();
			client.uberClient = new Jframe(args);
		} catch (Exception var2) {
			
		}
		/* Field streamField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Stream.class, "stream");
		Stream oldStream = null, newStream = null;
		try {
			oldStream = (Stream) streamField.get(client.uberClient);
			newStream = new StreamHook(oldStream.buffer);
			streamField.set(client.uberClient, newStream);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newStream.currentOffset = oldStream.currentOffset; */
		EventManager.addListener(new GenericListener());
		new PrivilegeCommand();
		new ToggleCommand();
		new SpawnObjectCommand();
		new BankCommand();
		new MassTradeModule();
		new NoClipModule();
		new ESPModule();
		new PrayerModule();
		new KeepItemsModule();
		KeyBinding.addDispatcher();
		EventManager.addListener(new CommandListener());
		EventManager.addListener(new ModuleListener());
	}
	public static final String NAME = "Waffen";
}
