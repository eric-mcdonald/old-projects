

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ESPModule extends ModuleBase {

	public ESPModule() {
		super("ESP", "Draws a box around an object", ModuleTypeEnum.RENDER);
		this.setKeyBinding(new KeyBinding(this, KeyEvent.VK_E));
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
		Field main3DAreaField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, RSImageProducer.class, "aRSImageProducer_1165" /* "main3DArea" */);
		try {
			RSImageProducer main3DArea = (RSImageProducer) main3DAreaField.get(client.uberClient);
			if (main3DArea != null) {
				main3DArea.initDrawingArea();
				Method npcScreenPosMethod = ReflectionHelper.findMethod(client.class, Modifier.PRIVATE, Void.TYPE, "npcScreenPos", new Class[] {Entity.class, Integer.TYPE});
				Field spriteDrawXField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "spriteDrawX"), spriteDrawYField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "spriteDrawY");
				Player[] playerArray = (Player[]) ReflectionHelper.findField(client.class, Modifier.PRIVATE, Player[].class, "playerArray").get(client.uberClient);
				for (int i = 0; i < playerArray.length; ++i) {
					Player p = playerArray[i];
					if (p == null || p == client.myPlayer) {
						continue;
					}
					npcScreenPosMethod.invoke(client.uberClient, p, p.height);
					int k2 = client.myPlayer.x - p.x, i3 = client.myPlayer.y - p.y, j = (int) Math.sqrt(k2 * k2 + i3 * i3), k = spriteDrawXField.getInt(client.uberClient), i1 = spriteDrawYField.getInt(client.uberClient);
					if (k == -1) {
						continue;
					}
					npcScreenPosMethod.invoke(client.uberClient, p, p.height * 2);
					int j1 = spriteDrawXField.getInt(client.uberClient), k1 = j > 999 ? 65280 : j > 499 ? 16776960 : j > 249 ? 16744448 : 16711680;
					if (j1 == -1) {
						continue;
					}
					DrawingArea.drawUnfilledPixels(k - 25, Math.abs(k - j1 + 50), Math.abs(i1 - spriteDrawYField.getInt(client.uberClient)), k1, i1);
					int i2 = k1;
					if ((i2 & -67108864) == 0) {
						i2 |= -16777216;
					}
					i2 = (i2 & 16579836) >> 2 | i2 & -16777216;
					client.uberClient.aTextDrawingArea_1271.method382(k1, k, p.name, i1 - 4, true);
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

}
