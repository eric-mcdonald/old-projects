

import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

public class NoClipModule extends ModuleBase {

	private Class11[] savedClass11Array = new Class11[4];
	private int[][] savedIntArray = new int[103][103];
	public NoClipModule() {
		super("No Clip", "Enables the ability to walk through walls", ModuleTypeEnum.MOVEMENT);
		this.setKeyBinding(new KeyBinding(this, KeyEvent.VK_N));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 103; j++) {
				for(int k = 1; k < 103; k++) {
					this.savedClass11Array[i].anIntArrayArray294[j][k] = this.savedIntArray[j][k];
				}
			}
		}
		this.savedClass11Array = new Class11[4];
		this.savedIntArray = new int[103][103];
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 103; j++) {
				for (int k = 1; k < 103; k++) {
					try {
						this.savedClass11Array[i] = (Class11) Array.get(ReflectionHelper.findField(client.class, Modifier.PRIVATE, Class11[].class, "aClass11Array1230").get(client.uberClient), i);
					} catch (ArrayIndexOutOfBoundsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.savedIntArray[j][k] = this.savedClass11Array[i].anIntArrayArray294[j][k];
				}
			}
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 103; j++) {
				for (int k = 1; k < 103; k++) {
					this.savedClass11Array[i].anIntArrayArray294[j][k] = 0;
				}
			}
		}
	}

}
