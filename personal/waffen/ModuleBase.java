


public abstract class ModuleBase implements Module {

	private String name, description;
	private ModuleTypeEnum type;
	private KeyBinding keyBinding;
	private boolean enabled;
	public ModuleBase(String name, String description, ModuleTypeEnum type) {
		this.name = name;
		this.description = description;
		this.type = type;
		if (MODULES.contains(this)) {
			throw new ModuleException("List already contains a module with the specified parameters: " + this);
		}
		MODULES.add(this);
	}
	@Override
	public boolean equals(Object obj) {
		Module module = null;
		if (obj instanceof Module) {
			module = (Module) obj;
		}
		return module != null ? module.getName().equals(this.name) && module.getDescription().equals(this.description) && module.getType().equals(this.type) && module.getKeyBinding() == this.keyBinding : false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}

	@Override
	public KeyBinding getKeyBinding() {
		// TODO Auto-generated method stub
		return this.keyBinding;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public ModuleTypeEnum getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}

	@Override
	public void setKeyBinding(KeyBinding keyBinding) {
		// TODO Auto-generated method stub
		this.keyBinding = keyBinding;
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		boolean enabled = !this.enabled;
		if (enabled) {
			this.onEnable();
		}
		else {
			this.onDisable();
		}
		this.enabled = enabled;
	}
}
