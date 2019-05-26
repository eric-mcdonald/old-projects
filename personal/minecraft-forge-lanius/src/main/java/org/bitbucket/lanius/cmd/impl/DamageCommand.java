package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.util.CommandUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public final class DamageCommand extends ModCommand {

	private static final double Y_OFFSET = 0.0625D;

	private static boolean canDamage() {
		double yOffset = Y_OFFSET;
		final double EXPAND_VEC = -0.0625D;
		final AxisAlignedBB playerBox = Lanius.mc.player.getEntityBoundingBox();
		for (final AxisAlignedBB collidingBox : Lanius.mc.player.world.getCollisionBoxes(Lanius.mc.player,
				playerBox.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, yOffset, 0.0D))) {
			yOffset = collidingBox.calculateYOffset(playerBox, yOffset);
		}
		return yOffset >= Y_OFFSET && (!Lanius.mc.player.capabilities.isCreativeMode
				&& Lanius.mc.player.world.checkBlockCollision(
						playerBox.grow(EXPAND_VEC, EXPAND_VEC, EXPAND_VEC).expand(0.0D, -0.55D, 0.0D))
				|| Lanius.mc.player.capabilities.isCreativeMode);
	}

	private static void damage(float hearts) {
		hearts *= 2.0F;
		final int loopAmount = MathHelper.ceil(16.0F * hearts + (3.0F - Lanius.mc.player.fallDistance) * 16.0F);
		if (loopAmount <= 0) {
			return;
		}
		for (int i = 0; i <= loopAmount; i++) {
			Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayer.Position(Lanius.mc.player.posX,
					Lanius.mc.player.posY + Y_OFFSET, Lanius.mc.player.posZ, false));
			Lanius.mc.player.connection.getNetworkManager().sendPacket(new CPacketPlayer.Position(Lanius.mc.player.posX,
					Lanius.mc.player.posY, Lanius.mc.player.posZ, i == loopAmount));
		}
	}

	public DamageCommand() {
		super("damage", "dam");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<hearts>";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length <= 0, sender);
		if (!canDamage()) {
			CommandUtils.addText(sender,
					"Cannot damage the player because the damage cannot be dealt under these conditions.");
			return;
		}
		final float hearts = Float.parseFloat(args[0]), MIN_HEARTS = 0.5F;
		if (hearts < MIN_HEARTS) {
			throw new NumberInvalidException("commands.generic.num.tooSmall", hearts, MIN_HEARTS);
		}
		damage(hearts);
		CommandUtils.addText(sender, "Damaged the player by " + hearts + " hearts.");
	}

}
