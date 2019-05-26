import java.awt.event.KeyEvent;
import java.lang.reflect.Modifier;


public class PrayerModule extends ModuleBase {

	public PrayerModule() {
		super("Prayer", "Automatically renews the player's prayer points", ModuleTypeEnum.COMBAT);
		this.setKeyBinding(new KeyBinding(this, KeyEvent.VK_P));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (Double.parseDouble(RSInterface.interfaceCache[4012].message) / Double.parseDouble(RSInterface.interfaceCache[4013].message) * 100.0D == 0.0D) {
			try {
				Stream stream = (Stream) ReflectionHelper.findField(client.class, Modifier.PRIVATE, Stream.class, "stream").get(client.uberClient);
				stream.createFrame(132);
				stream.method433(ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseX").getInt(client.uberClient) + (client.myPlayer.x - 6 >> 7));
				stream.writeWord(409);
				stream.method432(ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "baseY").getInt(client.uberClient) + (client.myPlayer.y - 6 >> 7));
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
