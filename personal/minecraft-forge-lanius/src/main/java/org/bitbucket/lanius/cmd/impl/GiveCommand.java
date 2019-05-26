package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidItemException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.cmd.WrongGameModeException;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;

public final class GiveCommand extends ModCommand {

	public GiveCommand() {
		super("give");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<item> [amount] [data] [dataTag]";
	}

	@Override
	protected void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if (!Lanius.mc.player.capabilities.isCreativeMode) {
			throw new WrongGameModeException("Creative");
		}
		assertUsage(args.length <= 0, sender);
		args[0] = args[0].toLowerCase();
		final Item item = Item.getByNameOrId(args[0]);
		if (item == null) {
			throw new InvalidItemException(args[0]);
		}
		final ItemStack prevStack = Lanius.mc.player.inventoryContainer.getSlot(InventoryUtils.HOTBAR_BEGIN).getStack();
		final int amount = args.length >= 2 ? Integer.parseInt(args[1]) : 1,
				data = args.length >= 3 ? Integer.parseInt(args[2]) : 0;
		final ItemStack newStack = new ItemStack(item, amount, data);
		String message = "Given the player a stack of " + amount + " " + args[0] + " with metadata " + data;
		if (args.length >= 4) {
			final String tagJson = getChatComponentFromNthArg(sender, args, 3).getUnformattedText();
			try {
				newStack.setTagCompound(JsonToNBT.getTagFromJson(tagJson));
			} catch (final NBTException nbtEx) {
				throw new CommandException("commands.give.tagError", new Object[] { nbtEx.getMessage() });
			}
			message += " and data tag " + tagJson;
		}
		InventoryUtils.putStackInHotbar(newStack);
		InventoryUtils.clickWindow(InventoryUtils.HOTBAR_BEGIN, 1, ClickType.THROW);
		InventoryUtils.putStackInHotbar(prevStack);
		CommandUtils.addText(sender, message + ".");
	}

}
