

import java.awt.event.KeyEvent;
import java.lang.reflect.Modifier;
// import com.gunrun66.waffen.util.ReflectionHelper;
// import com.runescape.client.src.Stream;

public class MassTradeModule extends ModuleBase {

	public MassTradeModule() {
		super("Mass Trade", "Spams players with trade requests", ModuleTypeEnum.OTHER);
		this.setKeyBinding(new KeyBinding(this, KeyEvent.VK_T));
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
		Stream stream = null;
		try {
			stream = (Stream) ReflectionHelper.findField(client.class, Modifier.PRIVATE, Stream.class, "stream").get(client.uberClient);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (/* client.uberClient. */stream.currentOffset < 2500) {
			for (int i = 0; i < 400; i++) {
				if (/* client.uberClient. */stream.currentOffset >= 2500) {
					break;
				}
				/* client.uberClient. */stream.createFrame(139 /* 39 */);
				/* client.uberClient. */stream.method431(i);
			}
		}
	}

}
