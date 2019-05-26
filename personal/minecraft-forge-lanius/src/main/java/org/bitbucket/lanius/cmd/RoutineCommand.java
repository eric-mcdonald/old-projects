package org.bitbucket.lanius.cmd;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.Routine;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public abstract class RoutineCommand extends ModCommand {

	public RoutineCommand(final String name, final String... aliases) {
		super(name, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected final String getParamUsage() {
		// TODO Auto-generated method stub
		return "<routine> [routine2]";
	}

	protected abstract void handleRoutine(final Routine foundRoutine, ICommandSender sender, String[] args);

	@Override
	public final void modExec(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		assertUsage(args.length < 1, sender);
		for (int argIdx = 0; argIdx < args.length; argIdx++) {
			final Routine foundRoutine = Lanius.getInstance().getRoutineRegistry().get(args[argIdx]);
			if (foundRoutine == null) {
				throw new InvalidFeatureException(args[argIdx]);
			}
			handleRoutine(foundRoutine, sender, args);
		}
	}

}
