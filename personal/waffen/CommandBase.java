

public abstract class CommandBase implements Command {

	private String name, description, usage;
	public CommandBase(String name, String description, String usage) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		if (COMMANDS.contains(this)) {
			throw new CommandException("List already contains a command with the specified parameters: " + this);
		}
		COMMANDS.add(this);
	}
	@Override
	public boolean equals(Object obj) {
		Command command = null;
		if (obj instanceof Command) {
			command = (Command) obj;
		}
		return command != null ? command.getName().equals(this.name) && command.getDescription().equals(this.description) && command.getUsage().equals(this.usage) : false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return this.usage;
	}

}
