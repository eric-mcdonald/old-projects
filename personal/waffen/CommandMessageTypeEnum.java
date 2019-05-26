

public enum CommandMessageTypeEnum {

	INFO("INFO"), WARNING("WARNING"), ERROR("ERROR");
	private String name;
	private CommandMessageTypeEnum(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}
