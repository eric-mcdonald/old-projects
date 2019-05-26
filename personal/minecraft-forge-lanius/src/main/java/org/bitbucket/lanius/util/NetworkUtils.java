package org.bitbucket.lanius.util;

import org.bitbucket.lanius.Lanius;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public final class NetworkUtils {
	public static boolean digBlockPacket(final Packet<?> packet) {
		if (!(packet instanceof CPacketPlayerDigging)) {
			return false;
		}
		final CPacketPlayerDigging.Action action = ((CPacketPlayerDigging) packet).getAction();
		return action.equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)
				|| action.equals(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
				|| action.equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK);
	}

	public static int lagTicks() {
		return MathHelper.ceil(lagTime() / 50.0F);
	}

	public static int lagTime() {
		return !RoutineUtils.lagEnabled() || Lanius.mc.isSingleplayer() ? 0
				: lagTime(Lanius.mc.player.connection.getPlayerInfo(Lanius.mc.player.getName()));
	}

	public static int lagTime(NetworkPlayerInfo netPlayerInfo) {
		return netPlayerInfo == null ? 0 : Math.max(netPlayerInfo.getResponseTime(), 0);
	}

	public static boolean motionPacket(final Packet<?> packet) {
		if (!(packet instanceof CPacketPlayer)) {
			return false;
		}
		final CPacketPlayer playerPacket = (CPacketPlayer) packet;
		return (Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
				"field_149480_h", "moving")
				|| (Boolean) ObfuscationReflectionHelper.getPrivateValue(CPacketPlayer.class, playerPacket,
						"field_149481_i", "rotating");
	}
}
