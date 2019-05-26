package net.hackforums.pootpoot.monumental.mode;

import net.hackforums.pootpoot.monumental.Monumental;
import net.minecraft.util.IChatComponent;

public class DisplayText {

	private IChatComponent name, value;
	private int color;
	public DisplayText(IChatComponent name, IChatComponent value, int color) {
		if (name == null) {
			throw new NullPointerException(Monumental.NAME + ": name cannot be null");
		}
		this.name = name;
		this.value = value;
		this.color = color;
	}
	public DisplayText(IChatComponent name, int color) {
		this(name, null, color);
	}
	@Override
	public boolean equals(Object obj) {
		return obj != null ? obj.toString().equals(toString()) : false;
	}
	public int getColor() {
		return color;
	}
	public IChatComponent getDisplayComponent() {
		IChatComponent name = getName();
		if (value != null) {
			return name.appendText(" [").appendSibling(getValue()).appendText("]");
		}
		return name;
	}
	public IChatComponent getName() {
		return name.createCopy();
	}
	public IChatComponent getValue() {
		return value != null ? value.createCopy() : null;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public void setValue(IChatComponent value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "DisplayText{name=" + name + ", value=" + value + ", color=" + color + "}";
	}
}
