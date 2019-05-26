package org.bitbucket.lanius.cmd.impl;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.InvalidAttrException;
import org.bitbucket.lanius.cmd.InvalidItemException;
import org.bitbucket.lanius.cmd.ModCommand;
import org.bitbucket.lanius.cmd.WrongGameModeException;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.InventoryUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public final class NanCommand extends ModCommand {

	public NanCommand() {
		super("nanattributeitem", "nanattritem", "nanitem", "nan");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getParamUsage() {
		// TODO Auto-generated method stub
		return "<item> [size] [attrName] [operation] [equipSlot]";
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
		final int stackSz = args.length >= 2 ? Integer.parseInt(args[1]) : 64;
		final ItemStack newStack = new ItemStack(item, stackSz);
		String message;
		if (args.length < 3) {
			InventoryUtils.addAllNanAttributes(newStack);
			message = "Given the player a stack of " + stackSz + " " + args[0]
					+ " with all NaN attributes and operations (in all equipment slots).";
		} else {
			IAttributeInstance attribute = null;
			for (IAttributeInstance attr : Lanius.mc.player.getAttributeMap().getAllAttributes()) {
				if (attr.getAttribute().getName().equalsIgnoreCase(args[2])) {
					attribute = attr;
					break;
				}
			}
			if (attribute == null) {
				throw new InvalidAttrException(args[2]);
			}
			if (args.length < 5) {
				for (EntityEquipmentSlot equipSlot : EntityEquipmentSlot.values()) {
					if (args.length < 4) {
						for (int operation = 0; operation < 2; operation++) {
							InventoryUtils.addNanAttribute(newStack, equipSlot, attribute, operation);
						}
					} else {
						InventoryUtils.addNanAttribute(newStack, equipSlot, attribute, Integer.parseInt(args[3]));
					}
				}
				if (args.length < 4) {
					message = "Given the player a stack of " + stackSz + " " + args[0] + " with NaN "
							+ attribute.getAttribute().getName() + " and all operations (in all equipment slots).";
				} else {
					message = "Given the player a stack of " + stackSz + " " + args[0] + " with NaN "
							+ attribute.getAttribute().getName() + " and operation " + Integer.parseInt(args[3])
							+ " (in all equipment slots).";
				}
			} else {
				if (args.length < 4) {
					for (int operation = 0; operation < 2; operation++) {
						InventoryUtils.addNanAttribute(newStack, EntityEquipmentSlot.fromString(args[4]), attribute,
								operation);
					}
				} else {
					InventoryUtils.addNanAttribute(newStack, EntityEquipmentSlot.fromString(args[4]), attribute,
							Integer.parseInt(args[3]));
				}
				if (args.length < 4) {
					message = "Given the player a stack of " + stackSz + " " + args[0] + " with NaN "
							+ attribute.getAttribute().getName() + " and all operations when equipped in the " + args[4]
							+ " slot.";
				} else {
					message = "Given the player a stack of " + stackSz + " " + args[0] + " with NaN "
							+ attribute.getAttribute().getName() + " and operation " + Integer.parseInt(args[3])
							+ " when equipped in the " + args[4] + " slot.";
				}
			}
		}
		InventoryUtils.putStackInHotbar(newStack);
		InventoryUtils.clickWindow(InventoryUtils.HOTBAR_BEGIN, 1, ClickType.THROW);
		InventoryUtils.putStackInHotbar(prevStack);
		CommandUtils.addText(sender, message);
	}

}
