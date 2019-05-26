package org.bitbucket.lanius.util;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.Routine;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public final class CommandUtils {

	public static void addEnabledMsg(ICommandSender sender, final Routine routine) {
		CommandUtils.addText(sender, RoutineUtils.stateText(routine, true) + " routine " + routine + ".");
	}

	public static void addMessage(ICommandSender sender, ITextComponent message) {
		if (sender == null) {
			return;
		}
		final ITextComponent prefix = new TextComponentString("[" + Lanius.NAME + "]: ");
		prefix.getStyle().setColor(TextFormatting.RED);
		prefix.appendSibling(message);
		message.getStyle().setColor(TextFormatting.RESET);
		sender.sendMessage(prefix);
	}

	public static void addText(ICommandSender sender, String text) {
		addMessage(sender, new TextComponentString(text));
	}

	public static String concatArgs(String[] args) {
		String argStr = "";
		for (int i = 0; i < args.length; i++) {
			argStr += args[i];
			if (i < args.length - 1) {
				argStr += " ";
			}
		}
		return argStr;
	}

}
