package org.bitbucket.lanius.routine;

import java.io.PrintWriter;

public interface RoutineDataParser {
	void loadRoutine(Routine routine, String value);

	void saveRoutine(PrintWriter out, Routine routine);
}
