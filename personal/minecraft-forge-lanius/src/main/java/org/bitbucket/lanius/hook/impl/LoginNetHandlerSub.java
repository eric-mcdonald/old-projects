package org.bitbucket.lanius.hook.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.gui.McLeaksGui;
import org.bitbucket.lanius.hook.HookManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.CryptManager;
import net.minecraft.util.Session;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public final class LoginNetHandlerSub extends NetHandlerLoginClient {

	private static final Logger LOGGER = LogManager.getLogger();

	private final NetworkManager networkManager;

	public LoginNetHandlerSub(NetworkManager networkManagerIn, Minecraft mcIn, GuiScreen previousScreenIn) {
		super(networkManagerIn, mcIn, previousScreenIn);
		networkManager = networkManagerIn;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleEncryptionRequest(SPacketEncryptionRequest packetIn) {
		HookManager.netHook.setOfflineMode();
		final SecretKey secretkey = CryptManager.createNewSharedKey();
		String s = packetIn.getServerId();
		PublicKey publickey = packetIn.getPublicKey();
		String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);

		if (McLeaksGui.isTokenActive()) {
			final String server = ((InetSocketAddress) networkManager.getRemoteAddress()).getHostName() + ":"
					+ ((InetSocketAddress) networkManager.getRemoteAddress()).getPort();
			try {
				final Session session = Lanius.mc.getSession();
				String jsonBody = "{\"session\":\"" + session.getToken() + "\",\"mcname\":\"" + session.getUsername()
						+ "\",\"serverhash\":\"" + s1 + "\",\"server\":\"" + server + "\"}";
				final HttpURLConnection joinConnection = (HttpURLConnection) new URL(
						"http://auth.mcleaks.net/v1/joinserver").openConnection();
				joinConnection.setConnectTimeout(10000);
				joinConnection.setReadTimeout(10000);
				joinConnection.setRequestMethod("POST");
				joinConnection.setDoOutput(true);
				final DataOutputStream outputStream = new DataOutputStream(joinConnection.getOutputStream());
				outputStream.write(jsonBody.getBytes("UTF-8"));
				outputStream.flush();
				outputStream.close();
				final BufferedReader joinReader = new BufferedReader(
						new InputStreamReader(joinConnection.getInputStream()));
				final StringBuilder joinJson = new StringBuilder();
				String line;
				while ((line = joinReader.readLine()) != null) {
					joinJson.append(line);
				}
				joinReader.close();
				final JsonElement responseElement = new Gson().fromJson(joinJson.toString(), JsonElement.class);
				if ((!responseElement.isJsonObject()) || (!responseElement.getAsJsonObject().has("success"))) {
					networkManager.closeChannel(new TextComponentString("Invalid response from MCLeaks API"));
					return;
				}
				if (!responseElement.getAsJsonObject().get("success").getAsBoolean()) {
					String errorMessage = "Received a failing response from MCLeaks API";
					if (responseElement.getAsJsonObject().has("errorMessage")) {
						errorMessage = responseElement.getAsJsonObject().get("errorMessage").getAsString();
					}
					networkManager.closeChannel(new TextComponentString(errorMessage));
					return;
				}
			} catch (Exception e) {
				networkManager
						.closeChannel(new TextComponentString("Error whilst contacting MCLeaks API: " + e.toString()));
				return;
			}
		} else if (Lanius.mc.getCurrentServerData() != null && Lanius.mc.getCurrentServerData().isOnLAN()) {
			try {
				Lanius.mc.getSessionService().joinServer(Lanius.mc.getSession().getProfile(),
						Lanius.mc.getSession().getToken(), s1);
			} catch (AuthenticationException var10) {
				LOGGER.warn("Couldn\'t connect to auth servers but will continue to join LAN");
			}
		} else {
			try {
				Lanius.mc.getSessionService().joinServer(Lanius.mc.getSession().getProfile(),
						Lanius.mc.getSession().getToken(), s1);
			} catch (AuthenticationUnavailableException var7) {
				this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo",
						new Object[] { new TextComponentTranslation("disconnect.loginFailedInfo.serversUnavailable",
								new Object[0]) }));
				return;
			} catch (InvalidCredentialsException var8) {
				this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo",
						new Object[] { new TextComponentTranslation("disconnect.loginFailedInfo.invalidSession",
								new Object[0]) }));
				return;
			} catch (AuthenticationException authenticationexception) {
				this.networkManager.closeChannel(new TextComponentTranslation("disconnect.loginFailedInfo",
						new Object[] { authenticationexception.getMessage() }));
				return;
			}
		}

		this.networkManager.sendPacket(new CPacketEncryptionResponse(secretkey, publickey, packetIn.getVerifyToken()),
				new GenericFutureListener<Future<? super Void>>() {
					@Override
					public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
						LoginNetHandlerSub.this.networkManager.enableEncryption(secretkey);
					}
				}, new GenericFutureListener[0]);
	}

}
