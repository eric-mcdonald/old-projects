package org.bitbucket.lanius.gui;

import java.io.IOException;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.util.ReflectHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;

public class LoginGui extends GuiScreen {
	private static Session oldSession = null;
	private static String prevSessionAlias, newSessionKey;

	public static String getNewSessionKey() {
		return newSessionKey;
	}

	public static Session getOldSession() {
		return oldSession;
	}

	public static String getPrevSessionAlias() {
		return prevSessionAlias;
	}

	public static void setNewSessionKey(String newSessionKey) {
		LoginGui.newSessionKey = newSessionKey;
	}

	public static void setOldSession(Session oldSession) {
		if (LoginGui.oldSession == null) {
			LoginGui.oldSession = oldSession;
		}
		NameProtectRoutine nameProtect = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		prevSessionAlias = nameProtect.getAlias(oldSession.getUsername());
		if (prevSessionAlias != null) {
			nameProtect.removeAlias(oldSession.getUsername());
		}
	}

	private GuiScreen parent;

	public LoginGui(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			String[] sessionMappings = new String[] { "field_71449_j", "session" };
			if (button.id == 0) {
				mc.displayGuiScreen(parent);
			} else if (button.id == 1) {
				NameProtectRoutine nameProtect = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
						.get("Name Protect");
				String newAlias = nameProtect.getAlias(mc.getSession().getUsername());
				ReflectHelper.setValue(Minecraft.class, mc, oldSession, sessionMappings);
				if (prevSessionAlias != null) {
					if (newAlias != null) {
						nameProtect.putTag(oldSession.getUsername(), newAlias);
					}
					prevSessionAlias = null;
				}
				if (newSessionKey != null) {
					nameProtect.removeAlias(newSessionKey);
					newSessionKey = null;
				}
				oldSession = null;
				mc.displayGuiScreen(parent);
			} else if (button.id == 2) {
				mc.displayGuiScreen(new OfflineGui(this));
			} else if (button.id == 3) {
				mc.displayGuiScreen(new McLeaksGui(this));
			}
			buttonList.get(1).enabled = oldSession != null;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "Login", width / 2, 20, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		buttonList.clear();
		buttonList
				.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.done", new Object[0])));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12, "Reset"));
		buttonList.get(1).enabled = oldSession != null;
		buttonList.add(new GuiButton(2, width / 2 - 100, 106, 98, 20, "Offline"));
		buttonList.add(new GuiButton(3, width / 2 + 2, 106, 98, 20, "MCLeaks"));
	}
}
