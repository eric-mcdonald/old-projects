package org.bitbucket.lanius.util;

import org.bitbucket.lanius.Lanius;
import org.bitbucket.lanius.routine.Routine;
import org.bitbucket.lanius.routine.impl.FlightRoutine;
import org.bitbucket.lanius.routine.impl.NoclipRoutine;

public final class RoutineUtils {
	public static String configName(final String name) {
		return name.toLowerCase().replace(' ', '_');
	}

	public static boolean enabled(final String routineName) {
		return Lanius.getInstance().getRoutineRegistry().get(routineName).isEnabled();
	}

	public static boolean flyEnabled() {
		return enabled("Flight") && !noclipEnabled()
				&& (((FlightRoutine) Lanius.getInstance().getRoutineRegistry().get("Flight")).isExecuting()
						|| !ncpEnabled() || ncpEnabled() && !viaVersionEnabled());
	}

	public static boolean lagEnabled() {
		return enabled("Lag Compensation");
	}

	public static boolean ncpEnabled() {
		return enabled("NoCheatPlus");
	}

	public static boolean noclipEnabled() {
		return enabled("Noclip")
				&& ((NoclipRoutine) Lanius.getInstance().getRoutineRegistry().get("Noclip")).executing();
	}

	public static String stateText(final Routine routine, final boolean capitalize) {
		final String state;
		if (routine.isEnabled()) {
			state = "a" + (capitalize ? "Enabled" : "enabled");
		} else {
			state = "c" + (capitalize ? "Disabled" : "disabled");
		}
		return "\247" + state + "\247r";
	}

	public static boolean viaVersionEnabled() {
		return enabled("ViaVersion");
	}
}
