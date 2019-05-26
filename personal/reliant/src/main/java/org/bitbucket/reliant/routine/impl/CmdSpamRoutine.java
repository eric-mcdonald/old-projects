package org.bitbucket.reliant.routine.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.bitbucket.eric_generic.lang.StringUtils;
import org.bitbucket.eric_generic.lang.time.Timer;
import org.bitbucket.reliant.Reliant;
import org.bitbucket.reliant.cfg.BaseConfigurable;
import org.bitbucket.reliant.cfg.BoolOption;
import org.bitbucket.reliant.cfg.ClampedNumber;
import org.bitbucket.reliant.cfg.Configurable;
import org.bitbucket.reliant.cfg.IntOption;
import org.bitbucket.reliant.memory.MemoryStream;
import org.bitbucket.reliant.routine.DisplayRoutine;

public final class CmdSpamRoutine extends DisplayRoutine {
	public final List<String> commands = new ArrayList<String>();
	public final Configurable commandsCfg = new BaseConfigurable(new File(Reliant.instance.getConfigsDir(), "command_spammer.txt")) {
		@Override
		public void load() {
			// TODO Auto-generated method stub
			commands.clear();
			if (!getConfigFile().exists()) {
				addDefaultCmds();
				return;
			}
			try {
				final BufferedReader configReader = new BufferedReader(new InputStreamReader(new FileInputStream(getConfigFile()), "UTF-8"));
				try {
					String entry;
					while ((entry = configReader.readLine()) != null) {
						if (StringUtils.comment(entry)) {
							continue;
						}
						commands.add(entry);
					}
				} catch (final IOException ioEx) {
					Reliant.instance.getLogger().logError(ioEx);
				} finally {
					try {
						configReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Reliant.instance.getLogger().logError(e);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e1);
			}
		}
		@Override
		public void save() {
			// TODO Auto-generated method stub
			getConfigFile().getParentFile().mkdirs();
			getConfigFile().delete();
			try {
				final PrintWriter configWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getConfigFile()), "UTF-8")));
				configWriter.println(StringUtils.COMMENT_PREFIX + " This is the custom configuration file of " + name() + ". It is not recommended that you edit this file directly.");
				configWriter.println(StringUtils.COMMENT_PREFIX + " Format of the entries is \"value\"");
				for (final String command : commands) {
					configWriter.println(command);
				}
				configWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Reliant.instance.getLogger().logError(e);
			}
		}
	};
	private final Timer intervalTimer = new Timer() {

		@Override
		protected long delay() {
			// TODO Auto-generated method stub
			return getInt("Interval");
		}
		
	};
	private int currentCmd;
	
	public CmdSpamRoutine() {
		super("Command Spammer", "Spams game commands.", false, true, false, 1001, new BoolOption("Unrestricted", "Specifies whether or not to execute restricted commands.", true), new IntOption("Interval", "Specifies the delay to wait.", new ClampedNumber<Integer>(1000, 0, 10000), 1000));
		addDefaultCmds();
		// TODO Auto-generated constructor stub
	}
	
	private void addDefaultCmds() {
		commands.add("say Download " + Reliant.NAME + " for VAC-undetected cheats!");
		commands.add("say bitbucket.org/eric_ptr/reliant");
	}

	@Override
	public int color() {
		// TODO Auto-generated method stub
		return 0xFFFF4000;
	}

	@Override
	public void reset(final boolean shutdown) {
		super.reset(shutdown);
		currentCmd = 0;
	}
	@Override
	public void update(final boolean post) {
		// TODO Auto-generated method stub
		if (post || commands.isEmpty() || !intervalTimer.delayPassed() || Reliant.instance.getProcessStream().moduleAddress("client.dll") == MemoryStream.NULL) {
			return;
		}
		if (currentCmd >= commands.size()) {
			currentCmd = 0;
		}
		if (getBoolean("Unrestricted")) {
			Reliant.instance.clientCmdRate.unrestricted = true;
			Reliant.instance.clientCmdRate.execute(commands.get(currentCmd++), 0);
		} else {
			Reliant.instance.clientCmdRate.execute(commands.get(currentCmd++), 0);
		}
		intervalTimer.setStartTime();
	}
}
