package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookManager;
import org.bitbucket.lanius.util.Phase;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTabComplete;

public final class PlayNetHandlerSub extends NetHandlerPlayClient {

	public PlayNetHandlerSub(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager networkManagerIn,
			GameProfile profileIn) {
		super(mcIn, p_i46300_2_, networkManagerIn, profileIn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleChangeGameState(SPacketChangeGameState packetIn) {
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START);
		super.handleChangeGameState(packetIn);
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.END);
	}

	@Override
	public void handleChat(SPacketChat packetIn) {
		if ((packetIn = (SPacketChat) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START)) == null) {
			return;
		}
		super.handleChat(packetIn);
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.END);
	}

	@Override
	public void handleCombatEvent(SPacketCombatEvent packetIn) {
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START);
		super.handleCombatEvent(packetIn);
	}

	@Override
	public void handleCustomSound(SPacketCustomSound packetIn) {
		if (HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START) == null) {
			return;
		}
		super.handleCustomSound(packetIn);
	}

	@Override
	public void handleEntityStatus(SPacketEntityStatus packetIn) {
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START);
		super.handleEntityStatus(packetIn);
	}

	@Override
	public void handleEntityVelocity(SPacketEntityVelocity packetIn) {
		if ((packetIn = (SPacketEntityVelocity) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START)) == null) {
			return;
		}
		super.handleEntityVelocity(packetIn);
	}

	@Override
	public void handleExplosion(SPacketExplosion packetIn) {
		if ((packetIn = (SPacketExplosion) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START)) == null) {
			return;
		}
		super.handleExplosion(packetIn);
	}

	@Override
	public void handleJoinGame(SPacketJoinGame packetIn) {
		packetIn = (SPacketJoinGame) HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START);
		super.handleJoinGame(packetIn);
	}

	@Override
	public void handlePlayerPosLook(SPacketPlayerPosLook packetIn) {
		if ((packetIn = (SPacketPlayerPosLook) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START)) == null) {
			return;
		}
		super.handlePlayerPosLook(packetIn);
		HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.END);
	}

	@Override
	public void handleRespawn(SPacketRespawn packetIn) {
		packetIn = (SPacketRespawn) HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START);
		super.handleRespawn(packetIn);
	}

	@Override
	public void handleSoundEffect(SPacketSoundEffect packetIn) {
		if (HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START) == null) {
			return;
		}
		super.handleSoundEffect(packetIn);
	}

	@Override
	public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetIn) {
		if (HookManager.packetManager.execute(new NetHandlerData(this, packetIn), Phase.START) == null) {
			return;
		}
		super.handleSpawnGlobalEntity(packetIn);
	}

	@Override
	public void handleSpawnPosition(SPacketSpawnPosition packetIn) {
		packetIn = (SPacketSpawnPosition) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START);
		super.handleSpawnPosition(packetIn);
	}

	@Override
	public void handleTabComplete(SPacketTabComplete packetIn) {
		if ((packetIn = (SPacketTabComplete) HookManager.packetManager.execute(new NetHandlerData(this, packetIn),
				Phase.START)) == null) {
			return;
		}
		super.handleTabComplete(packetIn);
	}

	@Override
	public void sendPacket(Packet<?> p_147297_1_) {
		if ((p_147297_1_ = HookManager.packetManager.execute(new NetHandlerData(this, p_147297_1_),
				Phase.START)) == null) {
			return;
		}
		super.sendPacket(p_147297_1_);
		HookManager.packetManager.execute(new NetHandlerData(this, p_147297_1_), Phase.END);
	}

}