package org.bitbucket.reliant.cfg;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.bitbucket.reliant.Main;
import org.bitbucket.reliant.Reliant;

public final class KeyOption extends TextOption<Integer> {
	public KeyOption(final String name, final String description, final Integer defaultVal) {
		super(name, description, defaultVal, 32);
		final JTextField guiComponent = (JTextField) guiComponent();
		guiComponent.setDocument(new PlainDocument() {
			/**
			 * The default serial version UID.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				try {
					parseValue(guiComponent.getText() + str);
				} catch (final NumberFormatException numberFormatEx) {
					return;
				}
				super.insertString(offs, str, a);
			}
		});
		guiComponent.setText(defaultVal.toString());
	}

	public boolean keyDown(final boolean checkMouse) {
		return keyDown(Main.getTargetWindow(), checkMouse);
	}

	private native boolean keyDown(final int key);

	public boolean keyDown(final String window, final boolean checkMouse) {
		if (Main.windowIsForeground(window) && (!checkMouse || !Main.mouseEnabled())) {
			final boolean keyboard = !Reliant.instance.isKeyboardEnabled() && (getValue() < 0x1 || getValue() > 0x6), mouse = !Reliant.instance.isMouseEnabled() && getValue() >= 0x1 && getValue() <= 0x6;
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			final boolean result = keyDown(getValue()) && Main.windowIsForeground(window) && (!checkMouse || !Main.mouseEnabled());
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			return result;
		}
		return false;
	}

	public boolean keyPressed(final boolean checkMouse) {
		return keyPressed(Main.getTargetWindow(), checkMouse);
	}

	private native boolean keyPressed(final int key);

	public boolean keyPressed(final String window, final boolean checkMouse) {
		if (Main.windowIsForeground(window) && (!checkMouse || !Main.mouseEnabled())) {
			final boolean keyboard = !Reliant.instance.isKeyboardEnabled() && (getValue() < 0x1 || getValue() > 0x6), mouse = !Reliant.instance.isMouseEnabled() && getValue() >= 0x1 && getValue() <= 0x6;
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			final boolean result = keyPressed(getValue());
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			return result;
		}
		return false;
	}

	@Override
	public Integer parseValue(final String value) {
		// TODO Auto-generated method stub
		return Integer.valueOf(value);
	}

	public int pressKey(final boolean release, final boolean checkMouse) {
		return pressKey(release, Main.getTargetWindow(), checkMouse);
	}

	public int pressKey(final boolean release, final String window, final boolean checkMouse) {
		if (Main.windowIsForeground(window) && (!checkMouse || !Main.mouseEnabled())) {
			final boolean keyboard = !Reliant.instance.isKeyboardEnabled() && (getValue() < 0x1 || getValue() > 0x6), mouse = !Reliant.instance.isMouseEnabled() && getValue() >= 0x1 && getValue() <= 0x6;
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			final int result = pressKey(getValue(), release);
			if (mouse) {
				Reliant.instance.setMouseEnabled();
			}
			if (keyboard) {
				Reliant.instance.setKeyboardEnabled();
			}
			return result;
		}
		return 0;
	}

	private native int pressKey(final int key, final boolean release);
}
