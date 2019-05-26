package shit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public final class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final URL url = new URL("https://twitter.com/alt4real");
		final BufferedReader in = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		final File arraysTxt = new File("array.txt");
		arraysTxt.delete();
		final PrintWriter out = new PrintWriter(new FileWriter(arraysTxt));
		out.print("var altQuotes = [");
		String line;
		while ((line = in.readLine()) != null) {
			final String PREFIX = "  <p class=\"TweetTextSize TweetTextSize--normal js-tweet-text tweet-text\" lang=\"en\" data-aria-label-part=\"0\">";
			if (line.startsWith(PREFIX)) {
				line = line.substring(PREFIX.length());
				line = line.substring(0, line.indexOf("</p>")).replace("&quot;", "\\\"");
				out.print("\"" + line + "\", ");
			}
		}
		out.print("];");
		out.close();
		in.close();
	}

}
