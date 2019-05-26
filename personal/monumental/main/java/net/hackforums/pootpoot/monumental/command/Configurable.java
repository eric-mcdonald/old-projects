package net.hackforums.pootpoot.monumental.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface Configurable {

	void load(BufferedReader reader) throws IOException;
	void save(PrintWriter writer);
}
