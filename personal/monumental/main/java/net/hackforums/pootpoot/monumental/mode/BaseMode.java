package net.hackforums.pootpoot.monumental.mode;

import net.hackforums.pootpoot.monumental.Monumental;
import net.hackforums.pootpoot.monumental.init.Modes;
import net.hackforums.pootpoot.monumental.util.MessageType;
import net.hackforums.pootpoot.monumental.util.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.apache.commons.lang3.ArrayUtils;

public abstract class BaseMode implements Mode {

	private IChatComponent name, description;
	private KeyBinding keyBind;
	private boolean enabled;
	public BaseMode(IChatComponent name, IChatComponent description) {
		this(name, description, null);
	}
	public BaseMode(IChatComponent name, IChatComponent description, KeyBinding keyBind) {
		if (name == null) {
			throw new NullPointerException(Monumental.NAME + ": name cannot be null");
		}
		if (description == null) {
			throw new NullPointerException(Monumental.NAME + ": description cannot be null");
		}
		this.name = name;
		this.description = description;
		this.keyBind = keyBind;
		if (MODES.contains(this)) {
			throw new ModeException(Monumental.NAME + ": duplicate Mode instantiated");
		}
		MODES.add(this);
		if (keyBind != null) {
			ClientRegistry.registerKeyBinding(keyBind);
		}
	}
	public BaseMode(String name, String description) {
		this(new ChatComponentText(name), new ChatComponentText(description));
	}
	public BaseMode(String name, String description, KeyBinding keyBind) {
		this(new ChatComponentText(name), new ChatComponentText(description), keyBind);
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null ? obj.toString().equals(toString()) : false;
	}

	@Override
	public IChatComponent getDescription() {
		// TODO Auto-generated method stub
		return description.createCopy();
	}

	@Override
	public KeyBinding getKeyBind() {
		// TODO Auto-generated method stub
		return keyBind;
	}

	@Override
	public IChatComponent getName() {
		// TODO Auto-generated method stub
		return name.createCopy();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	@Override
	public void onEnable() {
		if (!bypassesNoCheatPlus()) {
			MessageUtils.addMessage(Modes.NOCHEATPLUS_MODE.getName().appendText(": mode ").appendSibling(getName()).appendText(" does not bypass NoCheatPlus"), MessageType.WARNING);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		if (this.enabled != enabled) {
			this.enabled = enabled;
			if (enabled) {
				onEnable();
			}
			else {
				onDisable();
			}
		}
	}
	@Override
	public void setKeyBind(KeyBinding keyBind) {
		// TODO Auto-generated method stub
		if (keyBind == null && this.keyBind != null) {
			KeyBinding.setKeyBindState(this.keyBind.getKeyCode(), false);
			Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.removeElement(Minecraft.getMinecraft().gameSettings.keyBindings, this.keyBind);
		}
		this.keyBind = keyBind;
		if (keyBind != null) {
			ClientRegistry.registerKeyBinding(keyBind);
		}
	}
	@Override
	public String toString() {
		return "BaseMode{" + "name=" + name + ", description=" + description + ", keyBind=" + keyBind + ", enabled=" + enabled + "}";
	}
}
