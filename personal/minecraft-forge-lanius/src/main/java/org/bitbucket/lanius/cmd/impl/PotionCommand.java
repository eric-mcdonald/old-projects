package org.bitbucket.lanius.cmd.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.cmd.WrongGameModeException;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;

public class PotionCommand extends ModCommand {

	public PotionCommand() {
		super("potion", "pot");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<id|all|good|bad> [amount] [splash] [duration] [amplifier] [ambient] [particles]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if (!Lanius.mc.player.capabilities.isCreativeMode) {
			throw new WrongGameModeException("Creative");
		}
		assertUsage(args.length <= 0, sender);
		int id;
		try {
			id = Integer.parseInt(args[0]);
		} catch (NumberFormatException numEx) {
			id = -1;
			assertUsage(!args[0].equalsIgnoreCase("all") && !args[0].equalsIgnoreCase("good")
					&& !args[0].equalsIgnoreCase("bad"), sender);
		}
		ItemStack potStack = new ItemStack(
				args.length <= 2 || Boolean.parseBoolean(args[2]) ? Items.SPLASH_POTION : Items.POTIONITEM,
				args.length <= 1 ? 1 : Integer.parseInt(args[1]));
		String message = "Given the player a stack of " + potStack.getCount() + " " + potStack.getDisplayName();
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		if (id != -1) {
			Potion potion = Potion.getPotionById(id);
			if (potion == null) {
				throw new NumberInvalidException("commands.effect.notFound", args[0]);
			}
			effects.add(new PotionEffect(potion, args.length <= 3 ? Integer.MAX_VALUE : Integer.parseInt(args[3]),
					args.length <= 4 ? 127 : Integer.parseInt(args[4]),
					args.length <= 5 ? false : Boolean.parseBoolean(args[5]),
					args.length <= 6 ? true : Boolean.parseBoolean(args[6])));
			message += " with effect " + effects.get(0).getEffectName() + " " + effects.get(0).getAmplifier() + " for "
					+ effects.get(0).getDuration() + " as "
					+ (effects.get(0).getIsAmbient() ? "an ambient" : "a normal") + " potion "
					+ (effects.get(0).doesShowParticles() ? "that shows particles." : "that doesn't show particles.");
		} else {
			assertUsage(!args[0].equalsIgnoreCase("all") && !args[0].equalsIgnoreCase("good")
					&& !args[0].equalsIgnoreCase("bad"), sender);
			Iterator<Potion> potIt = Potion.REGISTRY.iterator();
			while (potIt.hasNext()) {
				Potion pot = potIt.next();
				if (!args[0].equalsIgnoreCase("all") && (args[0].equalsIgnoreCase("good") && pot.isBadEffect()
						|| args[0].equalsIgnoreCase("bad") && !pot.isBadEffect())) {
					continue;
				}
				PotionEffect effect = new PotionEffect(pot,
						args.length <= 3 ? Integer.MAX_VALUE : Integer.parseInt(args[3]),
						args.length <= 4 ? 127 : Integer.parseInt(args[4]),
						args.length <= 5 ? false : Boolean.parseBoolean(args[5]),
						args.length <= 6 ? true : Boolean.parseBoolean(args[6]));
				effects.add(effect);
			}
			if (!effects.isEmpty()) {
				message += " with "
						+ (args[0].equalsIgnoreCase("all") ? "all effects"
								: args[0].equalsIgnoreCase("good") ? "all good effects" : "all bad effects")
						+ " " + effects.get(0).getAmplifier() + " for " + effects.get(0).getDuration() + " as "
						+ (effects.get(0).getIsAmbient() ? "an ambient" : "a normal") + " potion "
						+ (effects.get(0).doesShowParticles() ? "that shows particles."
								: "that doesn't show particles.");
			}
		}
		PotionUtils.appendEffects(potStack, effects);
		ItemStack prevStack = Lanius.mc.player.inventoryContainer.getSlot(InventoryUtils.HOTBAR_BEGIN).getStack();
		InventoryUtils.putStackInHotbar(potStack);
		InventoryUtils.clickWindow(InventoryUtils.HOTBAR_BEGIN, 1, ClickType.THROW);
		InventoryUtils.putStackInHotbar(prevStack);
		CommandUtils.addText(sender, message);
	}

}
