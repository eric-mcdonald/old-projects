package org.bitbucket.lanius.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.impl.NameProtectRoutine;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public abstract class ModCommand extends CommandBase {
	static String prefix() {
		return Lanius.getInstance().getModCfg().getString("Prefix", "Command", ".",
				"Indicates that the chat message is a client command.");
	}

	private final String[] aliases;

	private final String name;

	public ModCommand(final String name, final String... aliases) {
		this.name = name;
		this.aliases = aliases;
	}

	protected final String argumentStr(final String[] args, final int startIdx) {
		String arguments = "";
		for (int argIdx = startIdx; argIdx < args.length; argIdx++) {
			arguments += args[argIdx];
			if (argIdx < args.length - 1) {
				arguments += " ";
			}
		}
		return arguments;
	}

	protected final void assertUsage(final boolean invalidCond, ICommandSender sender) throws WrongUsageException {
		if (invalidCond) {
			throw new WrongUsageException(getUsage(sender));
		}
	}

	@Override
	public final boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender.equals(Lanius.mc.player);
	}

	@Override
	public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		final NameProtectRoutine protectRoutine = (NameProtectRoutine) Lanius.getInstance().getRoutineRegistry()
				.get("Name Protect");
		if (protectRoutine.isEnabled()) {
			for (int argIdx = 0; argIdx < args.length; argIdx++) {
				for (final Entry<String, String> nameEntry : protectRoutine.nameEntries()) {
					args[argIdx] = args[argIdx].replace("-" + nameEntry.getValue(), nameEntry.getKey());
				}
			}
		}
		modExec(server, sender, args);
	}

	@Override
	public final List<String> getAliases() {
		String[] prefixAliases = new String[aliases.length];
		final String cmdPrefix = prefix();
		for (int aliasIdx = 0; aliasIdx < prefixAliases.length; aliasIdx++) {
			prefixAliases[aliasIdx] = cmdPrefix + aliases[aliasIdx];
		}
		return Arrays.asList(prefixAliases);
	}

	@Override
	public final String getName() {
		return prefix() + name;
	}

	protected abstract String getParamUsage();

	@Override
	public final int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public final String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		String usage = getName();
		if (!getAliases().isEmpty()) {
			for (String alias : getAliases()) {
				usage += "|" + alias;
			}
		}
		final String paramUsage = getParamUsage();
		if (!StringUtils.isNullOrEmpty(paramUsage)) {
			usage += " " + paramUsage;
		}
		return usage;
	}

	protected abstract void modExec(MinecraftServer server, ICommandSender sender, String[] args)
			throws CommandException;

	public final String[] names() {
		final List<String> names = new ArrayList<String>();
		names.add(name);
		for (final String alias : aliases) {
			names.add(alias);
		}
		return names.toArray(new String[0]);
	}

	@Override
	public final String toString() {
		return getName();
	}
}
