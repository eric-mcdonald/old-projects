

public enum ModuleTypeEnum {

	PLAYER("Player"), WORLD("World"), MOVEMENT("Movement"), OTHER("Other"), RENDER("Render"), COMBAT("Combat");
	private String name;
	private ModuleTypeEnum(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}
