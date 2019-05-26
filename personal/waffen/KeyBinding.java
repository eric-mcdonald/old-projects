

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class KeyBinding {

	public static void addDispatcher() {
		focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				// TODO Auto-generated method stub
				String input = null;
				try {
					input = (String) ReflectionHelper.findField(client.class, Modifier.PRIVATE, String.class, "inputString").get(client.uberClient);
					if ((e.getID() == KeyEvent.KEY_PRESSED || e.getID() == KeyEvent.KEY_RELEASED) && client.loggedIn && ToggleCommand.isKeyBindingsEnabled() && ((client.openInterfaceID != -1 && client.openInterfaceID == ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "reportAbuseInterfaceID").getInt(client.uberClient)) || ReflectionHelper.findField(client.class, Modifier.PRIVATE, Integer.TYPE, "backDialogID").getInt(client.uberClient) != -1 || input.length() == 0 || !input.startsWith("."))) {
						KeyBinding keyBinding = null;
						for (KeyBinding kb : keyBindings) {
							if (kb != null && kb.keyCode == e.getKeyCode()) {
								keyBinding = kb;
								break;
							}
						}
						if (keyBinding == null) {
							return false;
						}
						switch (e.getID()) {
						case KeyEvent.KEY_PRESSED:
							keyBinding.pressed = true;
							keyBinding.wasPressed = !keyBinding.wasPressed;
							break;
						case KeyEvent.KEY_RELEASED:
							if (!keyBinding.wasPressed) {
								keyBinding.pressed = false;
							}
							break;
						}
						EventManager.callEvent(new ToggleKeyBindingEvent(e, keyBinding));
					}
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return false;
			}

		});
	}
	public static List<KeyBinding> getKeyBindings() {
		return keyBindings;
	}
	private static Component focusOwner;
	private static List<KeyBinding> keyBindings = new ArrayList<KeyBinding>();
	private Module module;
	private int keyCode;
	private boolean pressed, wasPressed;
	public KeyBinding(Module module, int keyCode) {
		this.module = module;
		this.keyCode = keyCode;
		if (keyBindings.size() <= keyCode) {
			int size = keyCode - (keyBindings.size() - 1);
			for (int i = 0; i <= size; i++) {
				keyBindings.add(null);
			}
		}
		keyBindings.set(keyCode, this);
	}
	public int getKeyCode() {
		return this.keyCode;
	}
	public Module getModule() {
		return this.module;
	}
	public boolean isPressed() {
		return this.pressed;
	}
	public void setPressed(boolean pressed) {
		if (this.pressed != pressed) {
			this.pressed = pressed;
			this.wasPressed = false;
			EventManager.callEvent(new ToggleKeyBindingEvent(new KeyEvent(focusOwner, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, this.keyCode, Character.forDigit(this.keyCode, 10)), this));
		}
	}
}
