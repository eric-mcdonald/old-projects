package org.bitbucket.lanius.test;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.server.MinecraftServer;

public class SendRespawnCommand extends ModCommand {

	public SendRespawnCommand() {
		super("sendrespawn", "respawn");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		Lanius.mc.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
	}

}
