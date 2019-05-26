

import java.util.ArrayList;
import java.util.List;

public interface Command {

	List<Command> COMMANDS = new ArrayList<Command>();
	void execute(String command);
	String getDescription();
	String getName();
	String getUsage();
}
