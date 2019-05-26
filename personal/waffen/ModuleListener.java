

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ModuleListener {

	@SubscribeEvent
	public void onDrawGraphics(DrawGraphicsEvent event) {
		try {
			if (event.graphicalComponent == ReflectionHelper.findField(client.class, Modifier.PRIVATE, RSImageProducer.class, "aRSImageProducer_1165" /* "main3DArea" */).get(client.uberClient)) {
				for (Module m : Module.MODULES) {
					if (m.isEnabled() && m.getType().equals(ModuleTypeEnum.RENDER) && client.loggedIn) {
						m.update();
					}
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SubscribeEvent
	public void onProcessGameLoop(ProcessGameLoopEvent event) {
		for (Module m : Module.MODULES) {
			if (m.isEnabled() && !m.getType().equals(ModuleTypeEnum.RENDER) && client.loggedIn) {
				m.update();
			}
		}
	}
	@SubscribeEvent
	public void onToggleKeyBinding(ToggleKeyBindingEvent event) {
		if (event.keyEvent.getID() == KeyEvent.KEY_RELEASED && event.keyBinding.getModule() != null) {
			event.keyBinding.getModule().toggle();
			try {
				for (char c : (char[]) ReflectionHelper.findField(TextInput.class, Modifier.PRIVATE | Modifier.STATIC | Modifier.FINAL, char[].class, "validChars").get(null)) {
					if (c == event.keyEvent.getKeyChar()) {
						Field inputStringField = ReflectionHelper.findField(client.class, Modifier.PRIVATE, String.class, "inputString");
						String s = (String) inputStringField.get(client.uberClient);
						inputStringField.set(client.uberClient, s.substring(0, s.lastIndexOf(c)));
						break;
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
