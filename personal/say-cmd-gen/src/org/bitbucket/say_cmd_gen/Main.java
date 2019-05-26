package org.bitbucket.say_cmd_gen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public final class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 3) {
			System.exit(-1);
		}
		final boolean team = Boolean.parseBoolean(args[0]);
		for (int i = 1, j = 2; i < args.length && j < args.length; i++, j++) {
			final File inFile = new File(args[i]), outFile = new File(args[j]);
			if (!inFile.exists()) {
				continue;
			}
			outFile.delete();
			try {
				final BufferedReader in = new BufferedReader(new FileReader(inFile));
				final List<String> lines = new ArrayList<String>();
				String line;
				try {
					while ((line = in.readLine()) != null) {
						if (line.isEmpty()) {
							continue;
						}
						lines.add((team ? "say_team" : "say") + " " + line);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
					for (final String sayCmd : lines) {
						out.println(sayCmd);
					}
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
