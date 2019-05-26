

import java.awt.event.KeyEvent;

public class ToggleKeyBindingEvent implements Event {

	public KeyEvent keyEvent;
	public KeyBinding keyBinding;
	public ToggleKeyBindingEvent(KeyEvent keyEvent, KeyBinding keyBinding) {
		this.keyEvent = keyEvent;
		this.keyBinding = keyBinding;
	}
}
