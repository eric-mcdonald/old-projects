package org.bitbucket.lanius.cmd.impl;

import java.util.Map.Entry;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.routine.impl.BlinkRoutine;
import org.bitbucket.lanius.routine.impl.FreecamRoutine;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.NetworkUtils;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PingCommand extends ModCommand {

	public PingCommand() {
		super("ping");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "[player]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			CommandUtils.addText(sender, "Your ping is "
					+ NetworkUtils.lagTime(Lanius.mc.player.connection.getPlayerInfo(Lanius.mc.player.getName()))
					+ " ms.");
		} else {
			String playerName = CommandUtils.concatArgs(args);
			NameProtectRoutine nameProt = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
					.get("Name Protect");
			if (nameProt.isEnabled()) {
				for (Entry<String, String> protName : nameProt.nameEntries()) {
					playerName.replace("-" + protName.getValue(), protName.getKey());
				}
			}
			int ping = -1;
			for (EntityPlayer player : Lanius.mc.world.playerEntities) {
				if ((player instanceof EntityOtherPlayerMP || Lanius.mc.player.equals(player))
						&& !player.equals(((FreecamRoutine) Lanius.getInstance().getRoutineRegistry().get("Freecam"))
								.getRenderEntity())
						&& !player.equals(
								((BlinkRoutine) Lanius.getInstance().getRoutineRegistry().get("Blink")).getPosEntity())
						&& player.getName().equalsIgnoreCase(playerName)) {
					ping = NetworkUtils.lagTime(Lanius.mc.player.connection.getPlayerInfo(player.getName()));
					playerName = player.getName();
					break;
				}
			}
			if (ping == -1) {
				throw new PlayerNotFoundException("Player: " + playerName + " is invalid");
			}
			CommandUtils.addText(sender, playerName + "'s ping is " + ping + " ms.");
		}
	}
}
