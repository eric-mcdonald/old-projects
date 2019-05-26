package org.bitbucket.lanius.cmd;

import java.util.Iterator;
import java.util.Set;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cfg.ConfigContainer;
import org.bitbucket.lanius.hook.Hook;
import org.bitbucket.lanius.hook.impl.NetHandlerData;
import org.bitbucket.lanius.routine.impl.SpamRoutine;
import org.bitbucket.lanius.util.CommandUtils;
import org.bitbucket.lanius.util.Phase;

import net.minecraft.command.ICommand;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class CommandHandler extends ConfigContainer implements Hook<NetHandlerData> {
	@Override
	public String category() {
		// TODO Auto-generated method stub
		return "Command";
	}

	@Override
	public void onExecute(final NetHandlerData data, final Phase phase) {
		// TODO Auto-generated method stub
		if (phase.equals(Phase.START) && data.retVal instanceof CPacketChatMessage && !getBoolean("Send as Chat")
				&& !((SpamRoutine) Lanius.getInstance().getRoutineRegistry().get("Spam")).isAllowChat()) {
			final String message = ((CPacketChatMessage) data.retVal).getMessage();
			if (message.startsWith(ModCommand.prefix())) {
				data.retVal = null;
				CommandUtils.addText(Lanius.mc.player, "Command: " + message.split(" ")[0] + " is invalid.");
			}
		}
	}

	@SubscribeEvent
	public void onPostConfigChanged(final PostConfigChangedEvent postCfgChangedEv) {
		if (!postCfgChangedEv.getModID().equals(Lanius.MODID)) {
			return;
		}
		// Eric: re-register all Lanius commands for in case the prefix was
		// changed
		final Iterator<ICommand> valueIt = ClientCommandHandler.instance.getCommands().values().iterator();
		while (valueIt.hasNext()) {
			if (valueIt.next() instanceof ModCommand) {
				valueIt.remove();
			}
		}
		final Iterator<ICommand> cmdIt = ((Set<ICommand>) ObfuscationReflectionHelper.getPrivateValue(
				net.minecraft.command.CommandHandler.class, ClientCommandHandler.instance, "field_71561_b",
				"commandSet")).iterator();
		while (cmdIt.hasNext()) {
			if (cmdIt.next() instanceof ModCommand) {
				cmdIt.remove();
			}
		}
		Lanius.getInstance().registerCommands();
	}

	@Override
	public void registerValues() {
		// TODO Auto-generated method stub
		registerValue("Send as Chat", false, "Determines whether or not to send unknown commands as chat messages.");
	}
}
