package org.bitbucket.lanius.gui;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.util.ReflectHelper;
import org.lwjgl.input.Keyboard;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class McLeaksGui extends GuiScreen {
	private static final Gson gson = new Gson();

	private static boolean tokenActive;

	public static boolean isTokenActive() {
		return tokenActive;
	}

	private final GuiScreen parentScreen;

	private GuiTextField tokenField;

	McLeaksGui(final GuiScreen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.enabled) {
			final String[] sessionMappings = new String[] { "field_71449_j", "session" };
			if (button.id == 0) {
				try {
					final HttpURLConnection redeemConnection = (HttpURLConnection) new URL(
							"http://auth.mcleaks.net/v1/redeem").openConnection();
					redeemConnection.setRequestMethod("POST");
					redeemConnection.setConnectTimeout(10000);
					redeemConnection.setReadTimeout(10000);
					redeemConnection.setDoOutput(true);
					final DataOutputStream redeemStream = new DataOutputStream(redeemConnection.getOutputStream());
					redeemStream.write(("{\"token\":\"" + tokenField.getText() + "\"}").getBytes("UTF-8"));
					redeemStream.flush();
					redeemStream.close();
					final BufferedReader redeemReader = new BufferedReader(
							new InputStreamReader(redeemConnection.getInputStream()));
					final StringBuilder result = new StringBuilder();
					String line;
					while ((line = redeemReader.readLine()) != null) {
						result.append(line);
					}
					redeemReader.close();
					final JsonElement jsonElement = gson.fromJson(result.toString(), JsonElement.class);
					if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")
							|| !jsonElement.getAsJsonObject().get("success").getAsBoolean()
							|| !jsonElement.getAsJsonObject().has("result")
							|| !jsonElement.getAsJsonObject().get("result").isJsonObject()) {
						mc.displayGuiScreen(parentScreen);
						return;
					}
					final JsonObject redeemResult = jsonElement.getAsJsonObject().get("result").getAsJsonObject();
					if (!redeemResult.has("mcname") || !redeemResult.has("session")) {
						mc.displayGuiScreen(parentScreen);
						return;
					}
					final Session newSession = new Session(redeemResult.get("mcname").getAsString(), "",
							redeemResult.get("session").getAsString(), "legacy");
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
					tokenActive = true;
					mc.displayGuiScreen(parentScreen);
				} catch (IOException ioEx) {
					ioEx.printStackTrace();
				}
			} else if (button.id == 1) {
				mc.displayGuiScreen(parentScreen);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, "MCLeaks", width / 2, 20, 16777215);
		drawString(fontRenderer, "Alt Token", width / 2 - 100, 94, 10526880);
		tokenField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		tokenField = new GuiTextField(2, fontRenderer, width / 2 - 100, 106, 200, 20);
		tokenField.setFocused(true);
		tokenField.setMaxStringLength(128);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("gui.done", new Object[0])));
		buttonList.get(0).enabled = !tokenField.getText().isEmpty();
		buttonList.add(
				new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		tokenField.textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 15) {
			tokenField.setFocused(!tokenField.isFocused());
		}
		if (keyCode == 28 || keyCode == 156) {
			actionPerformed(buttonList.get(0));
		}
		buttonList.get(0).enabled = !tokenField.getText().isEmpty();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		tokenField.updateCursorCounter();
	}
}
