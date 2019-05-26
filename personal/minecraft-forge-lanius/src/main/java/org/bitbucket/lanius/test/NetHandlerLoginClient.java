package org.bitbucket.lanius.test;

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
import org.bitbucket.lanius.gui.McLeaksGui;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.util.CryptManager;
import net.minecraft.util.Session;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NetHandlerLoginClient implements INetHandlerLoginClient {
	private static final Logger LOGGER = LogManager.getLogger();
	private GameProfile gameProfile;
	private final Minecraft mc;
	private final NetworkManager networkManager;
	private final GuiScreen previousGuiScreen;

	public NetHandlerLoginClient(NetworkManager networkManagerIn, Minecraft mcIn, GuiScreen previousScreenIn) {
		this.networkManager = networkManagerIn;
		this.mc = mcIn;
		this.previousGuiScreen = previousScreenIn;
	}

	private MinecraftSessionService getSessionService() {
		return this.mc.getSessionService();
	}

	@Override
	public void handleDisconnect(SPacketDisconnect packetIn) {
		this.networkManager.closeChannel(packetIn.getReason());
	}

	@Override
	public void handleEnableCompression(SPacketEnableCompression packetIn) {
		if (!this.networkManager.isLocalChannel()) {
			this.networkManager.setCompressionThreshold(packetIn.getCompressionThreshold());
		}
	}

	@Override
	public void handleEncryptionRequest(SPacketEncryptionRequest packetIn) {
		final SecretKey secretkey = CryptManager.createNewSharedKey();
		String s = packetIn.getServerId();
		PublicKey publickey = packetIn.getPublicKey();
		String s1 = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);

		if (McLeaksGui.isTokenActive()) {
			final String server = ((InetSocketAddress) networkManager.getRemoteAddress()).getHostName() + ":"
					+ ((InetSocketAddress) networkManager.getRemoteAddress()).getPort();
			try {
				final Session session = mc.getSession();
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
				final StringBuilder out = new StringBuilder();
				String line;
				while ((line = joinReader.readLine()) != null) {
					out.append(line);
				}
				joinReader.close();
				final JsonElement responseElement = new Gson().fromJson(out.toString(), JsonElement.class);
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
		} else if (this.mc.getCurrentServerData() != null && this.mc.getCurrentServerData().isOnLAN()) {
			try {
				this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(),
						s1);
			} catch (AuthenticationException var10) {
				LOGGER.warn("Couldn\'t connect to auth servers but will continue to join LAN");
			}
		} else {
			try {
				this.getSessionService().joinServer(this.mc.getSession().getProfile(), this.mc.getSession().getToken(),
						s1);
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
						NetHandlerLoginClient.this.networkManager.enableEncryption(secretkey);
					}
				}, new GenericFutureListener[0]);
	}

	@Override
	public void handleLoginSuccess(SPacketLoginSuccess packetIn) {
		this.gameProfile = packetIn.getProfile();
		this.networkManager.setConnectionState(EnumConnectionState.PLAY);
		net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.fmlClientHandshake(this.networkManager);
		NetHandlerPlayClient nhpc = new NetHandlerPlayClient(this.mc, this.previousGuiScreen, this.networkManager,
				this.gameProfile);
		this.networkManager.setNetHandler(nhpc);
		net.minecraftforge.fml.client.FMLClientHandler.instance().setPlayClient(nhpc);
	}

	/**
	 * Invoked when disconnecting, the parameter is a ChatComponent describing the
	 * reason for termination
	 */
	@Override
	public void onDisconnect(ITextComponent reason) {
		if (this.previousGuiScreen != null && this.previousGuiScreen instanceof GuiScreenRealmsProxy) {
			this.mc.displayGuiScreen(
					(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy) this.previousGuiScreen).getProxy(),
							"connect.failed", reason)).getProxy());
		} else {
			this.mc.displayGuiScreen(new GuiDisconnected(this.previousGuiScreen, "connect.failed", reason));
		}
	}
}