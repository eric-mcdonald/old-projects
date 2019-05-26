package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.concurrent.Rate;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.server.MinecraftServer;

public final class VClipCommand extends ModCommand {

	public VClipCommand() {
		super("vclip", "vc");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<amount>";
	}

	@Override
	public void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length != 1, sender);
		final double amount = Double.parseDouble(args[0]);
		assertUsage(amount == 0.0D, sender);
		final double absAmount = Math.abs(amount);
		final boolean negAmount = amount < 0.0D;
		final Rate<CPacketPlayer> playerPacketRate = Lanius.getInstance().getPlayerPacketRate();
		boolean earlyStop = false;
		for (double current = 0.0D; current < absAmount; current += absAmount - current < 9.0D ? absAmount - current
				: 9.0D) {
			if (!playerPacketRate.execute(
					new CPacketPlayer.PositionRotation(Lanius.mc.player.posX,
							Lanius.mc.player.posY + (negAmount ? -current : current), Lanius.mc.player.posZ,
							Lanius.mc.player.rotationYaw, Lanius.mc.player.rotationPitch, Lanius.mc.player.onGround),
					0)) {
				Lanius.mc.player.setPositionAndUpdate(Lanius.mc.player.posX,
						Lanius.mc.player.posY + (negAmount ? -current : current), Lanius.mc.player.posZ);
				earlyStop = true;
				break;
			}
		}
		final String directionTxt = negAmount ? "down" : "up";
		if (earlyStop) {
			CommandUtils.addText(sender, "Couldn't move the player " + directionTxt + " by the full " + absAmount
					+ " blocks because the maximum amount of movement packets was reached.");
		} else {
			Lanius.mc.player.setPositionAndUpdate(Lanius.mc.player.posX, Lanius.mc.player.posY + amount,
					Lanius.mc.player.posZ);
		}
		CommandUtils.addText(sender, "Moved the player " + directionTxt + " by " + absAmount + " blocks.");
	}

}
