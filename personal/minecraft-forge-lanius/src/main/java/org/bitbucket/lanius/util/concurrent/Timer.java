package org.bitbucket.lanius.util.concurrent;

import java.util.TimerTask;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.cmd.impl.TimersCommand;
import org.bitbucket.lanius.util.CommandUtils;

public final class Timer {

	private final boolean countdown;
	private int hours, minutes, seconds;
	public final String name;
	private boolean timing;

	public Timer(final String name) {
		this.name = name;
		countdown = false;
	}

	public Timer(final String name, final int hours, final int minutes, final int seconds) {
		this.name = name;
		countdown = true;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Timer ? ((Timer) obj).name.equals(name) : false;
	}

	public synchronized int getHours() {
		return hours;
	}

	public synchronized int getMinutes() {
		return minutes;
	}

	public synchronized int getSeconds() {
		return seconds;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public synchronized void setTiming(final boolean timing) {
		this.timing = timing;
	}

	public void start() {
		this.setTiming(true);
		final java.util.Timer timer = new java.util.Timer();
		timer.schedule(new TimerTask() {

			@SuppressWarnings("unlikely-arg-type")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (!timing) {
					timer.cancel();
				}
				if (countdown) {
					seconds--;
					if (getSeconds() == -1) {
						minutes--;
						if (getMinutes() == -1) {
							hours--;
							if (getHours() == -1) {
								CommandUtils.addText(Lanius.mc.player, "Timer \"" + name + "\" has expired.");
								((TimersCommand) Lanius.getInstance().getCmdRegistry().get("timers")).timers
										.remove(this);
								timer.cancel();
							}
							minutes = 59;
						}
						seconds = 59;
					}
				} else {
					seconds++;
					if (getSeconds() == 60) {
						minutes++;
						if (getMinutes() == 60) {
							hours++;
							minutes = 0;
						}
						seconds = 0;
					}
				}
			}

		}, 1000L, 1000L);
	}

	@Override
	public String toString() {
		return name;
	}
}