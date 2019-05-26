package org.bitbucket.lanius.hook.impl;

import org.bitbucket.lanius.hook.HookData;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public final class NetHandlerData extends HookData<NetHandlerPlayClient, Packet> {

	public NetHandlerData(final NetHandlerPlayClient source, final Packet packet) {
		super(source);
		retVal = packet;
		// TODO Auto-generated constructor stub
	}

}
