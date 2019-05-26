package org.bitbucket.lanius.gui;

import java.io.IOException;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.util.ReflectHelper;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class OfflineGui extends GuiScreen {
	private GuiScreen parent;

	private GuiTextField usernameField;

	OfflineGui(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			final String[] sessionMappings = new String[] { "field_71449_j", "session" };
			if (button.id == 0) {
				final Session newSession = new Session(usernameField.getText(), "", "FML", "legacy");
				LoginGui.setOldSession(mc.getSession());
				ReflectHelper.setValue(Minecraft.class, mc, newSession, sessionMappings);
				final String[] sessionTypeMappings = new String[] { "field_152429_d", "sessionType" };
				ReflectHelper.setValue(Session.class, newSession, ObfuscationReflectionHelper.getPrivateValue(
						Session.class, LoginGui.getOldSession(), sessionTypeMappings), sessionTypeMappings);
				NameProtectRoutine nameProtect = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("Name Protect");
				if (LoginGui.getNewSessionKey() != null) {
					nameProtect.removeAlias(LoginGui.getNewSessionKey());
				}
				LoginGui.setNewSessionKey(mc.getSession().getUsername());
				if (LoginGui.getPrevSessionAlias() != null) {
					nameProtect.putTag(LoginGui.getNewSessionKey(), LoginGui.getPrevSessionAlias());
				}
				mc.displayGuiScreen(parent);
			} else if (button.id == 1) {
				mc.displayGuiScreen(parent);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Offline", width / 2, 20, 16777215);
		drawString(fontRenderer, "Username", width / 2 - 100, 94, 10526880);
		usernameField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		usernameField = new GuiTextField(2, fontRenderer, width / 2 - 100, 106, 200, 20);
		usernameField.setFocused(true);
		usernameField.setMaxStringLength(128);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("gui.done", new Object[0])));
		buttonList.get(0).enabled = !usernameField.getText().isEmpty();
		buttonList.add(
				new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		usernameField.textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 15) {
			usernameField.setFocused(!usernameField.isFocused());
		}
		if (keyCode == 28 || keyCode == 156) {
			actionPerformed(buttonList.get(0));
		}
		buttonList.get(0).enabled = !usernameField.getText().isEmpty();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		usernameField.updateCursorCounter();
	}
}
